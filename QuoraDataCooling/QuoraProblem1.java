import java.util.*;
import org.perf4j.*;

public class QuoraProblem1
{
	int[][] matrix;
	int row, width;
	AdjacencyMatrix graph;
	final static String SOURCE_NODE_LABEL = "Src";
	final static String SINK_NODE_LABEL = "Snk";
	
	
	public static void main(String[] args)
	{
		StopWatch stopWatch = new LoggingStopWatch("GraphGenerate");
		QuoraProblem1 r = new QuoraProblem1(Integer.valueOf(args[0]), Integer.valueOf(args[1]), args);
		r.generate();
		stopWatch.stop();
		stopWatch = new LoggingStopWatch("GraphPathFind");
//		r.listPaths("A", "I");
		r.listPaths(SOURCE_NODE_LABEL, SINK_NODE_LABEL);
		stopWatch.stop();
//		r.printInComingMatrix();
//		System.out.println("-------------");
//		r.printGraph();
	}
	
	public QuoraProblem1(int r, int w, String[] data)
	{
//		System.out.println(Arrays.toString(data));
		row = r; width = w;
		matrix = new int[row][];
		for(int i=0;i<row;i++)
			matrix[i] = new int[width];

		int index = 2;
		for(int i=0;i<row;i++)
			for(int j=0;j<width;j++)
				matrix[i][j] = Integer.valueOf(data[index++]);
	}

	
	private void listPaths(String start, String finish)
	{
		// Node -- List of AdjacentNodes
		ArrayList<Entry> traversalList = new ArrayList<Entry>();
		List<LinkedList<String>> pathList = new ArrayList<LinkedList<String>>();

		// add the Start Node and its adjacent nodes to the Stack
		Stack<String> adjacent = new Stack<String>();
		adjacent.addAll(getAdjacentList(start));
		Entry entry = new Entry(start, adjacent);
		traversalList.add(entry);
		
		int pathLengthSize = graph.size();
		System.out.println("Size = " + pathLengthSize);
		while(traversalList.size() != 0)
		{
//			printTraversalList(traversalList);
			// get the last element in the map
			Entry tempEntry = traversalList.get(traversalList.size()-1);		// get the last one added
			
			// get the adjacent node stack for the last Node in the list
			Stack<String> tempStack = tempEntry.getAdjacent();
			
			// get the Top Node from the adjacency Stack
			String nextNode = tempStack.pop();
			
//			System.out.println("List : " + tempEntry.getNodeName() + "   Popped Item : " + nextNode);
			
			Stack<String> adjacent1 = new Stack<String>();
			List<String> a1 = getAdjacentList(nextNode);
			if ( nextNode.equals(SINK_NODE_LABEL) && traversalList.size() == graph.size() - 1 )
			{
//				System.out.println("**** Path Found *****");

				// add The Last Node into the List and print the path
				Entry entry1 = new Entry(SINK_NODE_LABEL, adjacent1);
				traversalList.add(entry1);
//				printTraversalList(traversalList);
				addAndPrintPathFound(traversalList, pathList);
				// Path has been found - now prune it - so that all Entries with no elements in their stack are removed
				// this pruning happens from the end
				pruneTraversalList(traversalList);
				
				continue;
			}
			
			for(String n : a1)
			{
				if ( ! existsInTraversalList(n, traversalList) )
				{
					if ( ! n.equals(SINK_NODE_LABEL) )			// we will change it to the SNK_NODE_LABEL later on
						adjacent1.push(n);
					else				// if we have encountered the last node - add it only if there are n-1 elements in the list
					{
//						System.out.println("traversalList.size() = " + traversalList.size());
						if ( traversalList.size() == graph.size() - 2)
							adjacent1.push(n);
					}
				}
			}

			// if we have encountered a node - from which all Adjacent Nodes are already in the the traversalList
			// then we keep removing entries in the traversalList whose stack size() == 0
			if ( adjacent1.size() == 0 )
			{
//				System.out.println("adjacent1.size() : " + adjacent1.size());
				pruneTraversalList(traversalList);
			}
			else
			{
				Entry entry1 = new Entry(nextNode, adjacent1);
				traversalList.add(entry1);
			}
		}
		
		prinAllPaths(pathList);
	}

	private void prinAllPaths(List<LinkedList<String>> p)
	{
		for(int i=0;i<p.size();i++)
		{
			System.out.println("Path #" + i);
			System.out.println(p.get(i).toString());
		}
	}
	
	private void addAndPrintPathFound(List<Entry> l, List<LinkedList<String>> pathList)
	{
		LinkedList<String> path = new LinkedList<String>();
		for(int i=0;i<l.size();i++)
		{
//			System.out.print(" " + l.get(i).getNodeName());
			path.add(l.get(i).getNodeName());
		}
			
		pathList.add(path);
//		System.out.println();
	}
	
	private void pruneTraversalList(List<Entry> l)
	{
		int index = l.size() - 1;
		while(true)
		{
			if ( index < 0 )
				break;
			Entry e = l.get(index);
			if ( e.getAdjacent() == null )
			{
				l.remove(index);
				index--;
				continue;
			}
			
//			System.out.println("Entry : " + e.toString());
			if (e.getAdjacent().size() == 0)
			{
				l.remove(index);
				index--;
			}
			else
				break;
		}
	}
	
	private void printTraversalList(List<Entry> l)
	{
		for(Iterator it = l.iterator(); it.hasNext();)
		{
			Entry e = (Entry)it.next();
			System.out.println(e.toString());
		}
		System.out.println("----------------");
	}
	
	private	boolean existsInTraversalList(String node, ArrayList<Entry> traversalList)
	{
		for(Entry e : traversalList)
		{
			if ( e.getNodeName().equals(node) )
				return true;
		}
		return false;
	}
	
	
	private List<String> getAdjacentList(String node)
	{
		return graph.getAdjacentNodeList(node);
	}
	
	private void generate()
	{
		generateGraph();
//		renameGraphNodes();
		printGraph();
	}
	
	public void renameGraphNodes()
	{
		graph.updateNodeLabel(SOURCE_NODE_LABEL, "A");
		graph.updateNodeLabel(SINK_NODE_LABEL, "I");
		graph.updateNodeLabel("1-2", "B");
		graph.updateNodeLabel("1-3", "C");
		graph.updateNodeLabel("2-1", "F");
		graph.updateNodeLabel("2-2", "E");
		graph.updateNodeLabel("2-3", "D");
		graph.updateNodeLabel("2-4", "G");
		graph.updateNodeLabel("3-1", "H");
		graph.updateNodeLabel("3-2", "J");
		graph.updateNodeLabel("3-3", "K");
		graph.updateNodeLabel("3-4", "L");
	}
	
	// Adjacency Matrix - size of the graph will be row*width   X   row*width
	// number of nodes - roughly = row*width number of elements in the Input Matrix
	// we will make the Node with Label 2 as the Source and Label 3 Node as the sink.
	// we will not make any Nodes which has a label other than { 0, 2, 3} - since all other nodes
	// do not belong to the company
	// Labels of Nodes which has 0 in the Input Matrix will be the Coordinate(row,Column) in integer from the
	// input matrix
	private void generateGraph()
	{
		graph = new AdjacencyMatrix();
		for(int i=0;i<row;i++)
		{
			String prevNodeLabel = "";
			for(int j=0;j<width;j++)
			{
				if (( matrix[i][j] == 2 ) || ( matrix[i][j] == 0 ) || ( matrix[i][j] == 3 ))
				{
					String nodeLabel = Integer.valueOf(i+1).toString() + "-" + Integer.valueOf(j+1).toString();
					if ( matrix[i][j] == 2 ) nodeLabel = SOURCE_NODE_LABEL;
					if ( matrix[i][j] == 3 ) nodeLabel = SINK_NODE_LABEL;
					
					graph.addNode(nodeLabel);
					if ( prevNodeLabel.length() != 0 )
					{
						if ( !nodeLabel.equals(SOURCE_NODE_LABEL))				// do not add any Edge from any Node to the Source
							graph.addEdge(prevNodeLabel, nodeLabel);
						if ( !nodeLabel.equals(SINK_NODE_LABEL) && !prevNodeLabel.equals(SOURCE_NODE_LABEL))
							graph.addEdge(nodeLabel, prevNodeLabel);			// do not add any Edge from the Sink to any other Node
					}
					prevNodeLabel = nodeLabel;
				}
			}
		}
		for(int i=0;i<width;i++)
		{
			String prevNodeLabel = "";
			for(int j=0;j<row;j++)
			{
				if (( matrix[j][i] == 2 ) || ( matrix[j][i] == 0 ) || ( matrix[j][i] == 3 ))
				{
					String nodeLabel = Integer.valueOf(j+1).toString() + "-" + Integer.valueOf(i+1).toString();
					if ( matrix[j][i] == 2 ) nodeLabel = SOURCE_NODE_LABEL;
					if ( matrix[j][i] == 3 ) nodeLabel = SINK_NODE_LABEL;
					
					if ( prevNodeLabel.length() != 0 )
					{
						if ( !nodeLabel.equals(SOURCE_NODE_LABEL))			// do not add any Edge from any Node to the Source
							graph.addEdge(prevNodeLabel, nodeLabel);
						if ( !nodeLabel.equals(SINK_NODE_LABEL) && !prevNodeLabel.equals(SOURCE_NODE_LABEL))
							graph.addEdge(nodeLabel, prevNodeLabel);		// do not add any Edge from the Sink to any other Node
					}
					prevNodeLabel = nodeLabel;
				}
			}
		}

//		printGraph();
	}
	
	public void printGraph()
	{
		graph.printGraph();
	}
	
	public void printInComingMatrix()
	{
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<width;j++)
				System.out.print(matrix[i][j] + " ");
			System.out.println();
		}
	}
	
}

class Entry
{
		String NodeName;
		Stack<String> adjacentToNode;
		
		Entry()
		{
		}
		
		Entry(String name, Stack<String> a)
		{
			NodeName = name; 
			adjacentToNode = a;
		}
		
		public String getNodeName() { return NodeName; }
		public Stack<String> getAdjacent() { return adjacentToNode; }
		public String toString() 
		{
			return "Node: " + NodeName + ", Stack: " + adjacentToNode.toString();
		}
}
