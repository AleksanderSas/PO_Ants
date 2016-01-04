import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.Node;

public class Graph {
	
	private Map<String, GraphNode> nodes = new HashMap<>();
	private List<Path> paths = new ArrayList<>();
	private GraphNode startNode = null;
	
	public Graph(String fileName) throws DocumentException, GraphBuildException
	{
         parseXml(fileName);
         computMinDistance();
         
         //just warnings
         for(GraphNode graphNode : nodes.values())
        	 if(graphNode.getNodes().size() == 0)
        		 System.out.println(String.format("WARN: node %s has no paths", graphNode.getName()));
	}
	
	public boolean addPath(GraphNode node1, GraphNode node2, int distance)
	{
		Path path = new Path(distance,node1, node2);
		
		if(!node1.addPath(path, node2))
			return false;
		
		node2.addPath(path,  node1);	
		paths.add(path);
		return true;
	}
	
	/*
	 * 	The function computes minimal distance from
	 *  node to the start node, this is BFS indeed
	 */
	private void computMinDistance()
	{
		QueueSet<GraphNode> queue1 = new QueueSet<>();
		QueueSet<GraphNode> queue2 = new QueueSet<>();
		queue1.add(startNode);
		int distance = 0;
		GraphNode currentNode = null;
		
		while(!queue1.isEmpty() || !queue2.isEmpty())
		{
			while((currentNode = queue1.pull()) != null)
			{
				if(currentNode.minDistance == -1)
				{
					currentNode.minDistance = distance;
					queue2.add(currentNode.getNodes());
				}
			}
			distance++;
			
			while((currentNode = queue2.pull()) != null)
			{
				if(currentNode.minDistance == -1)
				{
					currentNode.minDistance = distance;
					queue1.add(currentNode.getNodes());
				}
			}
			distance++;
		}
	}
	
	private void parseXml(String fileName) throws DocumentException, GraphBuildException
	{
		File inputFile = new File(fileName);
        SAXReader reader = new SAXReader();
        Document document = reader.read( inputFile );

		List<Node> xlmNodes = document.selectNodes("/nodes/node" );
		//load empty nodes
        for(Node xmlNode : xlmNodes)
        {
       	 String nodeName = xmlNode.valueOf("@name");
       	 if(nodes.containsKey(nodeName))
       		 throw new GraphBuildException(String.format("Redefine node %s", nodeName));
       	 GraphNode newNode = new GraphNode(nodeName);
       	 nodes.put(nodeName, newNode);
       	 if(startNode == null)
       		 startNode = newNode;
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
        }
        
        for(GraphNode n : nodes.values())
        	System.out.println(String.format("new node added: %s - %d connectins", 
	       			 n.getName(), n.getNodes().size()));
        System.out.println(String.format("Loaded %d nodes", nodes.size()));
        System.out.println(String.format("Loaded %d paths", paths.size()));
        System.out.println(String.format("Branching factor %f", 2.0 * paths.size() / nodes.size()));
	}
	
	public void saveGraph2XML(String filename) throws IOException
	{
		Document doc = DocumentHelper.createDocument();
		Set<Path> addedPaths = new HashSet<>();
		
		Element nodes =  doc.addElement("nodes");
		addNode(nodes, startNode, addedPaths);
		for(GraphNode n : this.nodes.values())
		{
			if(n != startNode)
				addNode(nodes, n, addedPaths);
		}
		
		
		OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer;
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        writer = new XMLWriter(bw, format );
        writer.write( doc );
        bw.close();
	}
	
	private void addNode(Element root, GraphNode node, Set<Path> addedPaths)
	{
		Element XMLnode =  root.addElement("node").addAttribute("name", node.getName());
		for(Path p : node.getAllNodes())
		{
			if(!addedPaths.contains(p))
			{
				addedPaths.add(p);
				XMLnode.addElement("con").addAttribute("to", p.getNodeFrom(node).getName())
				.addText("" + p.getDistance());
			}
		}
	}
	
	public String printDistnce()
	{
		StringBuilder sb = new StringBuilder();
		for(GraphNode node : nodes.values())
			sb.append(node.getName()).append(" - ").append(node.minDistance).append("\n");
		return sb.toString();
	}
	
	public GraphNode getStartNode()
	{
		return startNode;
	}
	
	public Collection<Path> getPaths()
	{
		return paths;
	}
	
	public int getNodeNumber()
	{
		return nodes.size();
	}

}
