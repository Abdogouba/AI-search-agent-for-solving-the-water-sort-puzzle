package code;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class GenericSearch {
	
	private List<Stack<String>> initialState;
	private ArrayList<String> operators;
	private int nodesExpanded;
	
	public GenericSearch(List<Stack<String>> initialState, ArrayList<String> operators) {
		this.initialState = initialState;
		this.operators = operators;
	}

	public abstract boolean isGoal(List<Stack<String>> state);
	
	public abstract int calculatePathCost(Node node);
	
	public abstract Node search(List<Stack<String>> problem, String strategy);
	
	public abstract List<Stack<String>> transition(List<Stack<String>> state, String action);

	public List<Stack<String>> getInitialState() {
		return this.initialState;
	}
	
	public int getNodesExpanded() {
		return this.nodesExpanded;
	}
	
	public void setNodesExpanded(int nodesExpanded) {
		this.nodesExpanded = nodesExpanded;
	}

	public ArrayList<String> getOperators() {
		return operators;
	}
	
}
