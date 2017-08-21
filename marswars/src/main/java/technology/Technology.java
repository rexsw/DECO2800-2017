package technology;

import java.util.List;

public class Technology {
	//each tech thingo has id, Cost(r,c,w,b), Name, parent(list)
	private int id;
	private int[] cost;
	private String name;
	private List<Technology> parents;
	public Technology(int[] cost, String name, List<Technology> parents) {
		this.cost = cost;
		this.name = name;
		this. parents = parents;
		
	}
	
	public int[] getCost(){
		return cost;
	}
	
	public String getName(){
		return name;
	}
	
	public List<Technology> getParents(){
		return parents;
	}
	
}