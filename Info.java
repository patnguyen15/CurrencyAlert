/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package curreny;

/**
 *
 * @author Pat
 */
public class Info {
    private String Symbol;
    private double Target;
    private double readBid;
    public boolean finish;
    public boolean match;
    public int eleNum;
    private int highOrLow; // 1 if searching for low... 2 if searching for high
    
    public int getHighOrLow() {
		return highOrLow;
	}

	public void setLow() {
		this.highOrLow = 1;
	}
	public void setHigh() {
		this.highOrLow = 2;
	}

	Info()
    {
        Symbol = null;
        Target = 0;
        readBid = 0;
        finish = false;
        match = false;
        eleNum = 0;
    
    }
    
    public void setSymbol(String x)
    {
        Symbol = x;
    }
     public void setTarget(double x)
    {
        Target = x;
    }
     public void setReadBid(double x)
    {
        readBid = x;
    }
    public String getSymbol()
    {
       return Symbol;
    }
    public double getTarget()
    {
        return Target;
    }
    public double getReadBid()
    {
        return readBid;
    }
          
}
