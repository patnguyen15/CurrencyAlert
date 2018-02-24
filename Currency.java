
package curreny;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.Scanner; 

public class Currency {


	
   
    public static void main(String[] args) throws IOException, Exception
    {
        Scanner input = new Scanner(System.in);
        String tempSym;
        boolean complete = false;
        boolean match = false;
        int pairs;
        
        System.out.println("Please type how many pairs you want to search: ");
       
        // check if number
        while(!input.hasNextInt()){
        System.out.println("Error: you have not entered a valid number.");
        System.out.println("Please type how many pairs you want to search: ");
        input.next();
        
        }
    	pairs = input.nextInt();
   
        input.nextLine();
        
        Info selectedPairs[] = new Info[pairs];
        for(int d = 0; d<pairs; d++)
        {
            selectedPairs[d] = new Info();
        }
        // open connection 
        URL url = new URL("http://rates.fxcm.com/RatesXML");
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
        Document doc = parseXML(connection.getInputStream());
        
        NodeList pairRate = doc.getElementsByTagName("Rate");
        for(int z = 0; z < pairs; z++)
        {
            
                while(!selectedPairs[z].match)
                {
                    System.out.println("Please type in the symbol you would like to track: ");
                    tempSym = input.nextLine();
                    selectedPairs[z].setSymbol(tempSym);
                    for (int i = 0; i < pairRate.getLength(); i++)
                    {
						// ensures that the Symbol exist
                        Node testNode = pairRate.item(i);
                        Element eElement = (Element) testNode;
                        if(Objects.equals(selectedPairs[z].getSymbol(), eElement.getAttribute("Symbol")))
                        {
                            selectedPairs[z].eleNum = i;
                            selectedPairs[z].match = true;
                            System.out.println("Please type in a Target bid for " + selectedPairs[z].getSymbol() + ": ");
                            
                            // ensures user inputs an int or double ONLY
                            while(!input.hasNextDouble())
                            {
                                
                                System.out.println("Not a valid target");
                                System.out.println("Please type in a Target bid for " + selectedPairs[z].getSymbol() + ": ");
                                input.next();
                            }
                            
                            selectedPairs[z].setTarget(input.nextDouble());
                            while (selectedPairs[z].getTarget() < 0)
                            {
                            	System.out.println("Please enter a positive value for target.");
                                selectedPairs[z].setTarget(input.nextDouble());
                            }
                            input.nextLine();
                            if(selectedPairs[z].getTarget() < 0)
                            {
                                System.out.println("Warning: You inputted a negative Target. Target may already been reached");
                            }
                            

                            
                            break;
                            
                        }
                        else if(i == pairRate.getLength() - 1 && !match)
                        {
                            System.out.println("Symbol DNE\nPlease try again.");
                        }
                        
                    }
                }
                
        }
		
        NodeList descNodes = doc.getElementsByTagName("Bid");
        for(int l = 0; l < pairs; l++)
        {
			
            selectedPairs[l].setReadBid(Double.parseDouble(descNodes.item(selectedPairs[l].eleNum).getTextContent()));
            
            // Check to see if we are searching for a LOW or a HIGH
            double highOrLow = selectedPairs[l].getTarget() - selectedPairs[l].getReadBid();
            if (highOrLow < 0)
            {
            	selectedPairs[l].setLow();
            	System.out.println("Waiting for "+ selectedPairs[l].getSymbol() +" current bid to be lower than " + selectedPairs[l].getTarget());
            }
            if (highOrLow > 0)
            {
            	selectedPairs[l].setHigh();
            	System.out.println("Waiting for "+ selectedPairs[l].getSymbol() +" current bid to be higher than " + selectedPairs[l].getTarget());

            }
        }
        System.out.println("Target notification started.");
		
		// recursively check website for updated Bid
        while(complete == false)
        {
            connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.29 Safari/537.36");
            doc = parseXML(connection.getInputStream());
            
            descNodes = doc.getElementsByTagName("Bid");
            for(int w = 0; w < pairs; w++)
            {
                if(!selectedPairs[w].finish)
                {
                	
                	// this set is for searchings High
                    if(selectedPairs[w].getReadBid() <= selectedPairs[w].getTarget() && selectedPairs[w].getHighOrLow() == 2)
                    {
                    
                       
                        selectedPairs[w].setReadBid(Double.parseDouble(descNodes.item(selectedPairs[w].eleNum).getTextContent()));
                        //System.out.println(hello[w].getReadBid());
                    }
                    // If our High has been met
                    if(selectedPairs[w].getReadBid() >= selectedPairs[w].getTarget() && selectedPairs[w].getHighOrLow() == 2)
                    {
                        selectedPairs[w].finish = true;
                        System.out.println("Target reached for " + selectedPairs[w].getSymbol());
                        System.out.println("Current rate is " + selectedPairs[w].getReadBid()+ "\nYour Target was :"+ selectedPairs[w].getTarget());
                    }
                    
                    // This set is for searching Low
                    if(selectedPairs[w].getReadBid() >= selectedPairs[w].getTarget() && selectedPairs[w].getHighOrLow() == 1)
                    {
                        
                        selectedPairs[w].setReadBid(Double.parseDouble(descNodes.item(selectedPairs[w].eleNum).getTextContent()));
                        //System.out.println(hello[w].getReadBid());
                    }
                    // If our low has been met
                    if(selectedPairs[w].getReadBid() <= selectedPairs[w].getTarget() && selectedPairs[w].getHighOrLow() == 1)
                    {
                        selectedPairs[w].finish = true;
                        System.out.println("Target reached for " + selectedPairs[w].getSymbol());
                        System.out.println("Current rate is " + selectedPairs[w].getReadBid()+ "\nYour Target was :"+ selectedPairs[w].getTarget());
                    }
                    for(int b = 0;b < pairs; b++)
                    {
                        if(selectedPairs[b].finish)
                        {
                            complete = true;
                            
                        }
                        else
                        {
                            complete = false;
                            break;
                     
                        }
                      
                    }
                }
            }
            TimeUnit.SECONDS.sleep(10);
        }
       
        System.out.println("All targets reached. Complete");
    }
    
    private static Document parseXML(InputStream stream)
    throws Exception
    {
        DocumentBuilderFactory objDocumentBuilderFactory = null;
        DocumentBuilder objDocumentBuilder = null;
        Document doc = null;
        try
        {
            objDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
            objDocumentBuilder = objDocumentBuilderFactory.newDocumentBuilder();

            doc = objDocumentBuilder.parse(stream);
        }
        catch(Exception ex)
        {
            throw ex;
        }       

        return doc;
    }
    
    
}
