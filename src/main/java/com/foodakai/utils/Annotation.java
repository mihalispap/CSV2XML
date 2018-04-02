package com.foodakai.utils;

import java.util.ArrayList;

public class Annotation 
{
	public String value="";
	public String uri="";
	public float score;
	
	public String arn="";
	public String jsonid="";
	public String vocabulary=""; 

	public String reason="";
	
	public String toString()
	{
		return "jsonid:"+jsonid+",value:"+value+",URI:"+uri+",score:"+score;
		
	}
	public String toPrint()
	{
		return "";
	}
	
	public Annotation getMaxScore(ArrayList<Annotation> annotations, String value)
	{
		float max=0.0f;
		int index=-1;
		
		for(int i=0;i<annotations.size();i++)
		{
			if(annotations.get(i).value.equals(value) && max<annotations.get(i).score)
			{
				max=score;
				index=i;
			}
		}
		
		return annotations.get(index);
	}
	
	public int getIndexOfMaxScore(ArrayList<Annotation> annotations, String reason)
	{
		int index=-1;
		float max=-1.0f;
		
		for(int i=0;i<annotations.size();i++)
		{
			if(reason.equals(annotations.get(i).reason) && max<annotations.get(i).score)
			{
				index=i;
				max=annotations.get(i).score;
			}
		}
		
		return index;
	}
	
	/*
	 * Foreach enrichment reason, keep only highest ranking result
	 * */
	public void cleanse(ArrayList<Annotation> annotations)
	{
		int index=-1;
		for(int i=0;i<annotations.size();i++)
		{
			index=getIndexOfMaxScore(annotations, annotations.get(i).reason);
			
			for(int j=0;j<annotations.size();j++)
			{
				if(j!=index && annotations.get(i).reason.equals(annotations.get(j).reason))
				{
					annotations.remove(j);
					j--;
				}
			}
		}
	}
	
	public boolean exists(ArrayList<Annotation> annotations, int starting_index, String jsonid)
	{
		for(int i=starting_index;i<annotations.size();i++)
		{
			if(this.value.equals(annotations.get(i).value)
					&&
					this.jsonid.equals(annotations.get(i).jsonid)
					)
			{
				
				return true;
			}
		}
		return false;
	}
	
	public Annotation getMaxAnnotation(ArrayList<Annotation> annotations)
	{
		float max=0.0f;
		int index=0;
		
		for(int i=0;i<annotations.size();i++)
		{
			if(max<annotations.get(i).score)
			{
				index=i;
				max=annotations.get(i).score;
			}
		}
		return annotations.get(index);
	}
}

