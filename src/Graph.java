import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.Node;

public class Graph {
	
	private Map<String, GraphNode> nodes = new HashMap<>();
	private List<Path> paths = new ArrayList<>();
	
	public Graph(String filename) throws DocumentException, GraphBuildException
	{
		 File inputFile = new File(filename);
         SAXReader reader = new SAXReader();
         Document document = reader.read( inputFile );

		List<Node> xlmNodes = document.selectNodes("/nodes/node" );
		//load empty nodes
         for(Node xmlNode : xlmNodes)
         {
        	 String nodeName = xmlNode.valueOf("@name");
        	 if(nodes.containsKey(nodeName))
        		 throw new GraphBuildException(String.format("Redefine node %s", nodeName));
        	 nodes.put(nodeName, new GraphNode(nodeName));
         }
         //load path 
         for(Node xmlNode : xlmNodes)
         {
        	 String nodeName = xmlNode.valueOf("@name");
        	 GraphNode node1 = nodes.get(nodeName);
        	 List<Node> xlmsubNodes = xmlNode.selectNodes("./con");
        	 for(Node pathNode : xlmsubNodes)
        	 {
        		 GraphNode node2 = nodes.get(pathNode.valueOf("@to"));
        		 if(node2 == null)
        			 throw new GraphBuildException(String.format(
        					 "node %s is not defined", pathNode.valueOf("@to")));
        		 if(!addPath(node1, node2, new Integer(pathNode.getText())))
        			 throw new GraphBuildException(String.format("redefine path: %s - %s",
        					 node1.getName(), node2.getName()));
        	 }
        	 System.out.println(String.format("new node added: %s - %d new connectins", 
        			 nodeName, xlmsubNodes.size()));
         }
         
         //just warnings
         for(GraphNode graphNode : nodes.values())
        	 if(graphNode.getNodes().size() == 0)
        		 System.out.println(String.format("WARN: node %s has no paths", graphNode.getName()));
	}
	
	public boolean addPath(GraphNode node1, GraphNode node2, int distance)
	{
		Path path = new Path(distance);
		
		if(!node1.addPath(path, node2.getName()))
			return false;
		
		node2.addPath(path,  node1.getName());	
		paths.add(path);
		return true;
	}

}
