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
	
	public Graph(String filename) throws DocumentException
	{
		 File inputFile = new File(filename);
         SAXReader reader = new SAXReader();
         Document document = reader.read( inputFile );

		List<Node> xlmNodes = document.selectNodes("/nodes/node" );
         for(Node xmlNode : xlmNodes)
         {
        	 String nodeName = xmlNode.valueOf("@name");
        	 nodes.put(nodeName, new GraphNode());
         }
         for(Node xmlNode : xlmNodes)
         {
        	 String nodeName = xmlNode.valueOf("@name");
        	 GraphNode node1 = nodes.get(nodeName);
        	 List<Node> xlmsubNodes = xmlNode.selectNodes("./con");
        	 for(Node pathNode : xlmsubNodes)
        	 {
        		 GraphNode node2 = nodes.get(pathNode.valueOf("@to"));
        		 Path newPath = new Path(node1, node2, new Integer(pathNode.getText()));
        	 }
        	 System.out.println(String.format("new node addes: %s - %d new connectins", 
        			 nodeName, xlmsubNodes.size()));
         }
         


	}

}
