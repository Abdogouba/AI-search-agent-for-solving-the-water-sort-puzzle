package code;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public class WaterSortSearch extends GenericSearch {
	
	private int bottleCapacity;
	private int maxDepthIDS;
	private ArrayList<List<Stack<String>>> visitedStates;
	
	public WaterSortSearch(List<Stack<String>> initialState, ArrayList<String> operators, int bottleCapacity) {
		super(initialState, operators);
		this.bottleCapacity = bottleCapacity;
		this.visitedStates = new ArrayList<>(); 
	}
	
	public static String solve(String initialState, String strategy, boolean visualize) {
		
		String[] initialStateArray = initialState.split(";");
		
		int capacity = Integer.parseInt(initialStateArray[1]);
		
		List<Stack<String>> initialStateList = initialStateToList(initialStateArray);
		
		ArrayList<String> operators = new ArrayList<String>();
		operators.add("pour");
		
		WaterSortSearch wss = new WaterSortSearch(initialStateList, operators, capacity);
		
		Node solution;
		if(strategy.equals("ID"))
			solution = wss.runIDS();
		else
			solution = wss.search(initialStateList, strategy);
		
		if(solution == null)
			return "NOSOLUTION";
		else
			return wss.getFullSolution(solution);
		
	}

	private static String getPath(Node node) {
		if(node.getParent() == null)
			return "";
		return getPath(node.getParent()) + "," +  node.getOperatorFromParent();
	}
	
	private String getFullSolution(Node node) {
		
		String s = getPath(node);
		
		if(s.length() > 0)
			s = s.substring(1);
		
		return s + ";" + this.calculatePathCost(node) + ";" + this.getNodesExpanded();
		
	}

	private Node runIDS() {
		while(true) {	
			Node currentSolution = this.search(this.getInitialState(), "ID");
			
			if(currentSolution != null)
				return currentSolution;
			
			this.visitedStates = new ArrayList<>();
			
			this.maxDepthIDS++;
		}
	}

	private static List<Stack<String>> initialStateToList(String[] initialState) {
		
		List<Stack<String>> bottles = new ArrayList<>();
		
		for(int i = 2; i < initialState.length; i++) {
			Stack<String> bottle = new Stack<>();
			
			for(int j = initialState[i].length() - 1; j >= 0; j = j - 2)
				if(initialState[i].charAt(j) != 'e')
					bottle.push("" + initialState[i].charAt(j));
			
			bottles.add(bottle);
		}
		
		return bottles;
	}


	@Override
	public boolean isGoal(List<Stack<String>> state) {
		
		for(int i = 0; i < state.size(); i++) {
            for(int j = 0; j < state.get(i).size() - 1; j++) {
                if(!state.get(i).get(j).equals(state.get(i).get(j + 1)))
                	return false;
            }
        }
		
		return true;
		
	}


	@Override
	public int calculatePathCost(Node node) {
		return node.getPathCost();
	}


	@Override
	public Node search(List<Stack<String>> problem, String strategy) {
		
		Node initialState = new Node(problem, null, "", 0, 0);
		
		Queue<Node> nodes;
		
		if(strategy.equals("UC"))
			nodes = new PriorityQueue<>(Comparator.comparingInt(node -> node.getPathCost()));
		else if(strategy.equals("GR1") || strategy.equals("GR2"))
			nodes = new PriorityQueue<>(Comparator.comparingInt(node -> node.getPathCostToGoal()));
		else if(strategy.equals("AS1") || strategy.equals("AS2"))
			nodes = new PriorityQueue<>(Comparator.comparingInt(node -> node.getPathCostToGoal() + node.getPathCost()));
		else
			nodes = new LinkedList<>();
		
		nodes.add(initialState);
		
		this.visitedStates.add(initialState.getState());
		
		while(!nodes.isEmpty()) {
			Node current = nodes.remove();
			
			if(this.isGoal(current.getState()))
				return current;
			
			if(strategy.equals("ID") && (current.getDepth() + 1 > this.maxDepthIDS))
				continue;
			
			this.setNodesExpanded(this.getNodesExpanded() + 1);
			
			this.expand(current, nodes, strategy);
				
		}	 
			
		return null;
	}


	private void expand(Node current, Queue<Node> nodes, String strategy) {
		
		List<Stack<String>> bottles = current.getState();
		String s;
		String operator;
		int layersPoured;
		for(int i = 0; i < bottles.size(); i++) {
			for(int j = 0; j < bottles.size(); j++) {
				if(i != j && !bottles.get(i).isEmpty() && (bottles.get(j).isEmpty() || (bottles.get(j).size() < this.bottleCapacity && bottles.get(j).peek().equals(bottles.get(i).peek())))) {
					List<Stack<String>> bottlesTemp = clone(current.getState());
					operator = "pour" + "_" + i + "_" + j;
					layersPoured = 0;
					do {
						s = bottlesTemp.get(i).pop();
						bottlesTemp.get(j).push(s);
						layersPoured++;
					} while(!bottlesTemp.get(i).isEmpty() && (bottlesTemp.get(j).isEmpty() || (bottlesTemp.get(j).size() < this.bottleCapacity && bottlesTemp.get(j).peek().equals(bottlesTemp.get(i).peek()))));
					
					Node newNode = new Node(bottlesTemp, current, operator, current.getDepth() + 1, current.getPathCost() + layersPoured);
					
					this.addToQueue(newNode, nodes, strategy);
				}
			}
		}
	}

	private void addToQueue(Node newNode, Queue<Node> nodes, String strategy) {
		
		if(this.isRepeatedState(newNode.getState()))
			return;
		
		this.visitedStates.add(newNode.getState());
		
		switch(strategy) {
		
		case "BF": 
			nodes.add(newNode);
			break;
		
		case "DF": 
			((LinkedList<Node>) nodes).addFirst(newNode);
			break;
		
		case "ID": 
			((LinkedList<Node>) nodes).addFirst(newNode);
			break;
		
		case "UC": 
			nodes.add(newNode);			
			break;
		
		case "GR1": 
			calculatePathCostToGoal(newNode);
			nodes.add(newNode);			
			break;
			
		case "GR2": 
			calculatePathCostToGoal2(newNode);
			nodes.add(newNode);			
			break;	
			
		case "AS1": 
			calculatePathCostToGoal(newNode);
			nodes.add(newNode);			
			break;
			
		case "AS2": 
			calculatePathCostToGoal2(newNode);
			nodes.add(newNode);			
			break;
		}
		
	}

	private static void calculatePathCostToGoal(Node newNode) {
		
		List<Stack<String>> state = newNode.getState();
		
		int faultyBottles = 0;
		
		for(int i = 0; i < state.size(); i++) 
            for(int j = 0; j < state.get(i).size() - 1; j++)
                if(!state.get(i).get(j).equals(state.get(i).get(j + 1))) {
                	faultyBottles++;
                	break;
                }
            
		newNode.setPathCostToGoal(faultyBottles);
		
	}
	
	private static void calculatePathCostToGoal2(Node newNode) {
		
		List<Stack<String>> state = newNode.getState();
		
		int misplacedLayers = 0;
		String bottomColor = "";
		
		for(int i = 0; i < state.size(); i++) {
			if(!state.get(i).isEmpty())
				bottomColor = state.get(i).get(0);
			
			for(int j = 1; j < state.get(i).size(); j++) {
				if(!state.get(i).get(j).equals(bottomColor))
					misplacedLayers++;
			}
		}
		    
		newNode.setPathCostToGoal(misplacedLayers);
		
	}

	private boolean isRepeatedState(List<Stack<String>> state) {
		
		for(List<Stack<String>> visitedState : this.visitedStates) {
            if(areStatesEqual(state, visitedState)) {
                return true;
            }
        }
        return false;
		
	}

	private static boolean areStatesEqual(List<Stack<String>> state1, List<Stack<String>> state2) {
		
		// If the number of stacks is different, states are not equal
        if(state1.size() != state2.size()) {
            return false;
        }

        // Compare each stack in the state
        for(int i = 0; i < state1.size(); i++) {
            Stack<String> stack1 = state1.get(i);
            Stack<String> stack2 = state2.get(i);

            // If stack sizes are different, they are not equal
            if(stack1.size() != stack2.size()) {
                return false;
            }

            // Compare the elements in each stack
            for(int j = 0; j < stack1.size(); j++) {
                if(!stack1.get(j).equals(stack2.get(j))) {
                    return false;
                }
            }
        }

        return true;
		
	}

	private static List<Stack<String>> clone(List<Stack<String>> state) {
		
		List<Stack<String>> bottles = new ArrayList<>();
		
		for(int i = 0; i < state.size(); i++) {
			Stack<String> bottle = new Stack<>();
			
			for(int j = 0; j < state.get(i).size(); j++)
				bottle.push(state.get(i).get(j));
			
			bottles.add(bottle);
		}
		
		return bottles;
	}

	@Override
	public List<Stack<String>> transition(List<Stack<String>> state, String action) {
		
		return null;
	}

	
}