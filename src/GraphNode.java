import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.util.Pair;

public class GraphNode {
	//private Map<String, Pair<Path, GraphNode>> paths = new HashMap<>();
	//private Map<String, GraphNode> nodes = new HashMap<>();
	private Map<String, Path> paths = new HashMap<>();
	String name;
	public int minDistance;
	
	public GraphNode(String name)
	{
		this.name= name;
		minDistance = -1;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean addPath(Path path, GraphNode node)
	{
		if(paths.containsKey(node.getName()))
			return false;
		paths.put(node.getName(), path);
		//nodes.put(node.getName(), node);
		return true;
	}
	
	public Collection<GraphNode> getNodes()
	{
		return paths.values().stream().map(p -> p.getNodeFrom(this)).collect(Collectors.toList());
	}
	
	public Collection<Path>  getAllNodes()
	{
		return paths.values();
	}
	
	public Collection<Path>  getNodes(int maxDistance)
	{
		return paths.values().stream().filter(p -> p.getNodeFrom(this).minDistance <= maxDistance).
				collect(Collectors.toList());
	}
	
	public List<Path> getNodes(int maxDistance, Collection<GraphNode> visitedNodes)
	{
		return paths.values().stream().filter(p -> p.getNodeFrom(this).minDistance <= maxDistance 
				&& !visitedNodes.contains(p.getNodeFrom(this))).collect(Collectors.toList());
	}
	
	public Path getPath2Node(GraphNode node)
	{
		return paths.get(node.getName());
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		Collection<Path> paths = this.paths.values().stream().sorted((p1, p2) -> p1.compareTo(p2)).collect(Collectors.toList());
		
		for(Path p : paths)
		{
			sb.append(p.getNodeFrom(this).getName()).append("\t : ").append(p.getPheromon()).
			append("\t dts: ").append(p.getDistance()).append("\n");
		}
		return sb.toString();
	}
}
