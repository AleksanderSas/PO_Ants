import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	//choosing a parser parseXML or parseXML2
	public Graph(String fileName, int parser) throws DocumentException, GraphBuildException
	{
         if (parser == 1) parseXml(fileName);
         else parseXml2(fileName);
         computMinDistance();
         
         //just warnings
         for(GraphNode graphNode : nodes.values())
        	 if(graphNode.getNodes().size() == 0)
        		 System.out.println(String.format("WARN: node %s has no paths", graphNode.getName()));
	}
	
	public Graph(int n)
	{
		//buildGrapf_lader(n, 1, 2, 2, 5);
		buildGrapf_lader(n, 3, 1, 16, 16);
		computMinDistance();
	}
	
	public boolean addPath(GraphNode node1, GraphNode node2, float distance)
	{
		Path path = new Path(distance, node1, node2);
		
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
        
        private void parseXml2(String fileName) throws DocumentException, GraphBuildException
	{
		File inputFile = new File(fileName);
        SAXReader reader = new SAXReader();
        Document document = reader.read( inputFile );

		List<Node> xlmNodes = document.selectNodes("/travellingSalesmanProblemInstance/graph/vertex");
		//load empty nodes
	int licznik = 0;
        for(Node xmlNode : xlmNodes)
        {
       	 String nodeName = "node_number_" + licznik;
         licznik++;
       	 if(nodes.containsKey(nodeName))
       		 throw new GraphBuildException(String.format("Redefine node %s", nodeName));
       	 GraphNode newNode = new GraphNode(nodeName);
       	 nodes.put(nodeName, newNode);
       	 if(startNode == null)
       		 startNode = newNode;
        }
        //load path
	licznik = 0;
        for(Node xmlNode : xlmNodes)
        {
	       	 String nodeName = "node_number_" + licznik;
		 licznik++;
	       	 GraphNode node1 = nodes.get(nodeName);
	       	 List<Node> xlmsubNodes = xmlNode.selectNodes("./edge");
	       	 for(Node pathNode : xlmsubNodes)
	       	 {
                        Float f = new Float(pathNode.valueOf("@cost"));
	       		 GraphNode node2 = nodes.get("node_number_" +  pathNode.getText());
	       		 if(node2 == null)
	       			 throw new GraphBuildException(String.format(
	       					 "node %s is not defined", "node_number_" + pathNode.getText()));
	       		 addPath(node1, node2, f);
	       	 }
        }
        
        for(GraphNode n : nodes.values())
        	System.out.println(String.format("new node added: %s - %d connections", 
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
	
	/*	The function build structure line below:
	 * 
	 * 	(v_1_1)-w1-*-w1-*-...-w1-* -> w1 -> v_1_1
	 *		  |    |   |       |
	 *		 w2   w2   w2      w2
	 *		  |    |   |       |
	 * 		  *-w3-*-w3-*-...-w3-*
	 *		  |    |   |       |    
	 *		  w4  w4  w4      w4
	 *		  |    |   |       |
	 *     	 		v_1_1
	 *  
	 */
	//TODO refactor, remove to other class
	public void buildGrapf_lader(int size, int w1, int w2, int w3, int w4)
	{
		GraphNode previousNode_line1 = new GraphNode("v_1_1");
		GraphNode previousNode_line2 = new GraphNode("v_2_1");
		GraphNode currentNode_line1 = null;
		GraphNode currentNode_line2 = null;
		
		startNode = previousNode_line1;
		nodes.put("v_1_1", previousNode_line1);
		nodes.put("v_2_1", previousNode_line2);
		addPath( previousNode_line1, previousNode_line2, w2);
		
		for(int i = 2; i <= size; i++)
		{
			currentNode_line1 = new GraphNode("v_1_" + i);
			currentNode_line2 = new GraphNode("v_2_" + i);
			nodes.put("v_1_"  + i, currentNode_line1);
			nodes.put("v_2_"  + i, currentNode_line2);
			
			addPath(currentNode_line1, currentNode_line2, w2);
			addPath(currentNode_line1, previousNode_line1, w1);
			addPath(currentNode_line2, previousNode_line2, w3);
			addPath(currentNode_line2, startNode, w4);
			
			previousNode_line1 = currentNode_line1;
			previousNode_line2 = currentNode_line2;
		}
		
		addPath( previousNode_line1, startNode, w1);
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
	
	public void reset()
	{
		for(Path p : paths)
			p.reset();
	}

}
