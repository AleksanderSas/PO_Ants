import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Ant {

	private List<Path> road = new ArrayList<Path>();
	private List<GraphNode> visitedNodes = new ArrayList<>();
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
	public boolean walk(int nodeNumber) throws InternalException
	{
		//start node is already added
		while(nodeNumber > 1)
		{
			Collection<Path> paths = currentNode.getNodes(nodeNumber, visitedNodes);
			if(paths.size() == 0)
				//if(nodeNumber == 1)
					//break;
				//else
					return false;
			Path nextPath = Path.randPath(paths);
			road.add(nextPath);
			visitedNodes.add(nextPath.getNodeFrom(currentNode));
			currentNode = nextPath.getNodeFrom(currentNode);
			nodeNumber--;
		}
		Path lastPath = currentNode.getPath2Node(startNode);
		if(lastPath == null)
			return false;
		road.add(lastPath);
		complete = true;
		return true;
	}
	
	public void spreadPheromone(float pheromoneSpreadFactor)
	{
		if(! complete)
			return;
		float pheromone = 0;
		totalDistance = 0;
		
		for(Path p : road)
			totalDistance += p.getDistance();
		pheromone = pheromoneSpreadFactor / totalDistance;
		
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
			sb.append(n.getName()).append(" \tpher.: ").append(road.get(i).getPheromon()).append(" -> \n");
			i++;
		}
		sb.append("total distance: ").append(totalDistance);
		return sb.toString();
	}
}
