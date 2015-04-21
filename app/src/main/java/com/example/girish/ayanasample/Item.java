package com.example.girish.ayanasample;

public class Item {
	 
    private String title;
    private String description;
 
    public Item(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }
    // getters and setters...   
    public void setTitle(String t)
    {
    	title = t;
    }
    public void setDescription(String t)
    {
    	description = t;
    }
    public String getTitle()
    {
    	return title;
    }
    public String getDescription()
    {
    	return description;
    }
    public String toString()
    {
		return this.title;
    	
    }
}