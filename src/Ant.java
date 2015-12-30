import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Ant {

	private List<Path> road = new ArrayList<Path>();
	private List<GraphNode> visitedNodes = new ArrayList<>();
	private GraphNode currentNode = null;
	private GraphNode startNode;
	
	public Ant(GraphNode startNode)
	{
		currentNode = startNode;
		this.startNode = startNode;
		visitedNodes.add(startNode);
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
		while(nodeNumber > 0)
		{
			Collection<Path> paths = currentNode.getNodes(nodeNumber, visitedNodes);
			if(paths == null)
				return false;
			Path nextPath = Path.randPath(paths);
			road.add(nextPath);
			visitedNodes.add(nextPath.getNodeFrom(currentNode));
			currentNode = nextPath.getNodeFrom(currentNode);
			nodeNumber--;
		}
		road.add(currentNode.getPath2Node(startNode));
		return true;
	}
	
	public void reset()
	{
		currentNode = startNode;
		visitedNodes.clear();
		road.clear(); 
		visitedNodes.add(startNode);
	}
}
