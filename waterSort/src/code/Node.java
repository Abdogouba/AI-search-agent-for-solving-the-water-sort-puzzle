package code;

import java.util.List;
import java.util.Stack;

public class Node {
	
	private List<Stack<String>> state;
	private Node parent;
	private String operatorFromParent;
	private int depth;
	private int pathCost;
	private int pathCostToGoal;
	
	public Node(List<Stack<String>> state, Node parent, String operatorFromParent, int depth, int pathCost) {
		this.state = state;
		this.parent = parent;
		this.operatorFromParent = operatorFromParent;
		this.depth = depth;
		this.pathCost = pathCost;
	}
	
	public List<Stack<String>> getState() {
		return this.state;
	}
	
	public int getDepth() {
		return this.depth;
	}
	
	public int getPathCost() {
		return this.pathCost;
	}

	public Node getParent() {
		return this.parent;
	}

	public String getOperatorFromParent() {
		return this.operatorFromParent;
	}

	public int getPathCostToGoal() {
		return pathCostToGoal;
	}

	public void setPathCostToGoal(int pathCostToGoal) {
		this.pathCostToGoal = pathCostToGoal;
	}
	
	
	
}
