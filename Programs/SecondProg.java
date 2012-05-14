import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;


public class SecondProg{
    
	public static String validateData(String a, String b, String c, String d){
        
		
        String outcome ="";
        
        
        if(a.equals("sunny") && c.equals("high"))
        {outcome = "no";}
        
        else if (a.equals("sunny") && c.equals("normal"))
        {outcome = "yes";}
        
        else if (a.equals("overcast"))
        {outcome = "yes";} 
        
        else if (a.equals("rain") && d.equals("weak"))
        {outcome = "yes";}
        
        else if (d.equals("strong"))
        {outcome="no";}
        return outcome;       
	}
	
	public static double CalculateErorRate(double a, int b)
	{
		double errorRate;
		errorRate= ( a / b ) * 100;
		return errorRate;
	}
	
	
	
    public static void main(String[] args) {
        String outlook = "";
        String temper ="";
        String humidity = "";
        String wind = "";
        String play ="";
        int count=1;
        String input="";
        String actualOutcome="";
        double error=0;
        double errorRate=0.0;
        int exampleNumbers=0;
        int lineNumber = 0;
        try
        {
            
            // Training set which contains data
            String strFile = "Tennis_Example";
            
            
            //creating BufferedReader the training set
            BufferedReader b = new BufferedReader( new FileReader(strFile));
            String strLine = "";
            StringTokenizer st = null;
            int tokenNumber = 0;
            
            //read comma separated file line by line
            while( (strLine = b.readLine()) != null)
            {
                lineNumber++;
                exampleNumbers=lineNumber;
               
                st = new StringTokenizer(strLine, " ");
                
                while(st.hasMoreTokens())
                {
                    tokenNumber++;
                    input=st.nextToken();
                   
                    if (count==1)
                    {outlook=input;}
                    
                    if (count==2)
                    {temper=input;}
                    
                    if (count==3)
                    {humidity=input;}
                    
                    if (count==4)
                    {wind=input;}
                    
                    if (count==5)
                    { actualOutcome=input;}
                    
                    if (count==4)
                    { 
                    	play=validateData(outlook,temper,humidity,wind);
                    	System.out.println("THE PREDICTED ATTRIBUTE : " +play);
                    	
                    }
                    if (count==5)
                    {
                    	if(actualOutcome.equals(play))
                        {continue;}
                    	else
                    	{error=error+1;}
                    }
                    count++;   
                }
                
                //reset token number
                tokenNumber = 0;
                count=1;
                
            }
            
            errorRate=CalculateErorRate(error, lineNumber);
    		System.out.println("\n PERCENTAGE OF ERROR RATE= " +errorRate+"\n" );
        }
        catch(Exception e)
        {
            System.out.println("Exception while reading file: " + e);                  
        }
        
		
        
        
    }      
    
    
    
    
    
}

