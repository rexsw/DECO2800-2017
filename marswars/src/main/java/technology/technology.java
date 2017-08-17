package technology;

import java.util.List;

public class technology {
	//each tech thingo has id, Cost(r,c,w,b), Name, parent(list)
	private int id;
	private int[] cost;
	private String name;
	private List<technology> parents;
	public technology(int id, int[] cost, String name, List<technology> parents) {
		this.id = id;
		this.cost = cost;
		this.name = name;
		this. parents = parents;
		
	}
	
	public int getID(){
		return id;
	}
	
	public int[] getCost(){
		return cost;
	}
	
	public String getName(){
		return name;
	}
	
	public List<technology> getParents(){
		return parents;
	}
	
}
