package Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataList {
	
	private List<DataPair> pairs;
	private List<Integer> keys;
	
	int first = 0;
	
	
	private Map<Integer, DataPair> map;
	
	public DataList() {
		pairs = new ArrayList<>();
		keys = new ArrayList<>();
		
		map = new HashMap<>();
	}
	
	
	public void getKey(DataPair d)
	{
		
	}
	
	public void add(DataPair d)
	{
		if(!contains(d))
		{
			pairs.add(d);
			keys.add(first);
			first++;
		}
	}
	
	public boolean contains(DataPair d)
	{
		for (DataPair pair : pairs) {
			if((pair.getVertex() == d.getVertex()) && pair.getTexture() == d.getTexture())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public List<DataPair> getPairs() {
		return pairs;
	}
	public List<Integer> getKeys() {
		return keys;
	}

}
