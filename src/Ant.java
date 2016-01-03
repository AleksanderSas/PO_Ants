import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.util.Pair;

public class Ant implements Comparable<Ant>{

	private List<Path> road = new ArrayList<Path>();
	private List<GraphNode> visitedNodes = new ArrayList<>();
	private List<Float> ppb = new ArrayList<>();
	private GraphNode currentNode = null;
	private GraphNode startNode;
	public boolean complete = false;
	private int totalDistance;
	
	public Ant(GraphNode startNode)
	{
		this.startNode = startNode;
		reset();
	}
	
	/*
	 * 	PARAMETERS:
	 * 		nodeNumber: 	number of node to walk through
	 * 	RETURN
	 * 		true if found road successfully, otherwise false
	 * 		for example there is no next path from the node
	 */
	public boolean walk(int nodeNumber,int epoche) throws InternalException
	{
		//start node is already added
		totalDistance = 0;
		while(nodeNumber > 1)
		{
			List<Path> paths = currentNode.getNodes(nodeNumber, visitedNodes);
			if(paths.size() == 0)
				//if(nodeNumber == 1)
					//break;
				//else
					return false;
			Pair<Path, Float> pair = Path.randPath(paths,epoche);
			Path nextPath = pair.getKey();
			road.add(nextPath);
			visitedNodes.add(nextPath.getNodeFrom(currentNode));
			currentNode = nextPath.getNodeFrom(currentNode);
			ppb.add(pair.getValue());
			nodeNumber--;
			totalDistance += nextPath.getDistance();
		}
		Path lastPath = currentNode.getPath2Node(startNode);
		if(lastPath == null)
			return false;
		totalDistance += lastPath.getDistance();
		road.add(lastPath);
		ppb.add((float) -1.0);
		complete = true;
		return true;
	}
	
	public void spreadPheromone(float pheromoneSpreadFactor, int mean)
	{
		if(! complete)
			return;
		float pheromone = 0;
		//totalDistance = 0;
		
		//for(Path p : road)
			//totalDistance += p.getDistance();
		pheromone = pheromoneSpreadFactor / totalDistance; // * (mean - totalDistance);
		
		for(Path p : road)
			p.addPheromone(pheromone);
	}
	
	public void reset()
	{
		currentNode = startNode;
		visitedNodes.clear();
		road.clear();
		visitedNodes.add(startNode);
		complete = false;
		totalDistance = Integer.MAX_VALUE;
	}
	
	public int getDistance()
	{
		return totalDistance;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if(!complete)
		{
			return "ant terminated";
		}
		int i = 0;
		for(GraphNode n : visitedNodes)
		{
			sb.append(n.getName()).append(" \tpher.: ").append(road.get(i).getPheromon()).
			append("\tacc.:").append(ppb.get(i)).append(" -> \n");
			i++;
		}
		sb.append("total distance: ").append(totalDistance);
		return sb.toString();
	}

	@Override
	public int compareTo(Ant a) 
	{
		if(complete && !a.complete)
			return -1;
		if(!complete && a.complete)
			return 1;
		
		if(totalDistance < a.getDistance())
			return -1;
		return (totalDistance == a.getDistance())? 0 : 1;
	}
}
