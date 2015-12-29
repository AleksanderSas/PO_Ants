import java.util.HashMap;
import java.util.Map;

public class GraphNode {
	private Map<String, Path> nodes = new HashMap<>();
	String name;
	
	public GraphNode(String name)
	{
		this.name= name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean addPath(Path path, String nodeName)
	{
		if(nodes.containsKey(nodeName))
			return false;
		nodes.put(nodeName, path);
		return true;
	}
	
	public Map<String, Path> getNodes()
	{
		return nodes;
	}
}
