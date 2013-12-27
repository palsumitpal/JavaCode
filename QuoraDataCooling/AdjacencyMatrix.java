import java.util.*;

class AdjacencyMatrix
{
	List<BitSet> matrix;
	Map<String, Integer> lookUpNameToIndex;
	Map<Integer, String> lookUpIndexToName;
	int lastNodeIndex = 0;

	public AdjacencyMatrix()
	{
		matrix = new ArrayList<BitSet>();
		lookUpNameToIndex = new HashMap<String, Integer>();
		lookUpIndexToName = new HashMap<Integer, String>();
	}
	
	public void addNode(String name)
	{
		lookUpNameToIndex.put(name, lastNodeIndex);
		lookUpIndexToName.put(lastNodeIndex, name);
		matrix.add(lastNodeIndex, new BitSet());
		lastNodeIndex++;
	}
	
	// add an edge from n1 to n2
	public void addEdge(int n1, int n2)
	{
		BitSet b = matrix.get(n1);
		b.set(n2);
	}
	
	// add an edge from n1 to n2
	public void addEdge(String n1, String n2)
	{
		int n11 = lookUpNameToIndex.get(n1);
		int n12 = lookUpNameToIndex.get(n2);
		BitSet b = matrix.get(n11);
		b.set(n12);
	}
	
	
	public void printGraph()
	{
		System.out.println("Number of Nodes : " + lastNodeIndex);
		System.out.print("    ");
		for(int i=0;i<lastNodeIndex;i++)
			System.out.print(lookUpIndexToName.get(i) + "  ");
		
		System.out.println();
		for(int i=0;i<lastNodeIndex;i++)
		{
			System.out.print(lookUpIndexToName.get(i) + "  ");
			BitSet b = matrix.get(i);
			for(int j=0;j<lastNodeIndex;j++)
			{
				if(b.get(j))
					System.out.print(" 1 ");
				else
					System.out.print(" 0 ");
			}
			System.out.println("");
		}
	}

	public List<String> getAdjacentNodeList(String node)
	{
		List<String> nodeList = new ArrayList<String>();
		int index = lookUpNameToIndex.get(node);
		BitSet rowSet = matrix.get(index);
		int width = matrix.size();
		for(int i=0;i<width;i++)
		{
			if(rowSet.get(i))
				nodeList.add(lookUpIndexToName.get(i));
		}
		return nodeList;
	}
	
	BitSet visited = new BitSet();
	
	private boolean isVisited(String node)
	{
		return visited.get(lookUpNameToIndex.get(node));
	}
	
	private void setVisited(String node)
	{
		visited.set(lookUpNameToIndex.get(node));
	}
	
	public int size() { return matrix.size(); }
	
	public void updateNodeLabel(String oldNodeName, String newNodeName)
	{
		int index = lookUpNameToIndex.get(oldNodeName);
		String name = lookUpIndexToName.get(index);
		
		lookUpNameToIndex.remove(oldNodeName);
		lookUpNameToIndex.put(newNodeName, index);
		lookUpIndexToName.remove(index);
		lookUpIndexToName.put(index, newNodeName);
		
	}
	
	
/*	
	public static void main(String args[])
	{
		AdjacencyMatrix m = new AdjacencyMatrix();
		m.addNode("1");
		m.addNode("2");
		m.addNode("3");
		m.addNode("4");
		m.addEdge("1","2");
		m.addEdge("2","3");
		m.addEdge("3","4");
		
		m.printGraph();
	}
*/	
}
