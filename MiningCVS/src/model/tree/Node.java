/**
 * 
 */
package model.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Activity;
import model.Event;
import util.TreeUtils;

/**
 * @author Saimir Bala
 *
 */
public class Node {
	private String value;
	private boolean isAggregated;
	private Activity activity;
	private List<Event> eventList;
	private Node parent;
	private List<Node> childList;

	public Node() {
		this.eventList = new ArrayList<Event>();
		this.childList = new ArrayList<Node>();
	}

	public Node(String value) {
		this.value = value;
		this.eventList = new ArrayList<Event>();
		this.childList = new ArrayList<Node>();
	}

	public Node(String value, List<Event> eventList, Node parent,
			List<Node> childList) {
		super();
		this.value = value;
		this.eventList = eventList;
		this.parent = parent;
		this.childList = childList;
	}
	/**
	 * @return the childList
	 */
	public List<Node> getChildList() {
		return childList;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj==null)
			return this==null;
		Node o = (Node) obj;
		return value.equals(o.getValue());
	}

	/**
	 * @return the eventList
	 */
	public List<Event> getEventList() {
		return eventList;
	}
	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param childList the childList to set
	 */
	public void setChildList(List<Node> childList) {
		this.childList = childList;
	}

	/**
	 * @param eventList the eventList to set
	 */
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Node p) {
		parent = p;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "Node [value=" + value + ", isAggreated=" + isAggregated
				+ ", activity=" + activity + ", eventList=" + eventList
				+ ", parent=" + parent + ", childList=" + childList + "]";
	}

	public boolean hasChildren(){
		return this.childList==null || this.childList.isEmpty();
	}
	
	public void addChildren(List<Node> children){
		this.childList.addAll(children);
	}
	
	public void addChild(Node child){
		if(this.childList == null)
			this.childList = new ArrayList<Node>();
		this.childList.add(child);
	}

	/**
	 * @param str
	 * @return
	 */
   public Node getChild(String data) {
   	for(Node n : childList)
			if(n.value.equals(data))
				return n;
	   return null;
   }

	public boolean isAggreated() {
		return isAggregated;
	}

	public void setAggregated(boolean isAggreated) {
		this.isAggregated = isAggreated;
	}

	public Activity getActivity() {
		return activity;
	}
	
	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public Node copy(Node aggreNode, Activity a) {
		Node n = null;
		if(this!=aggreNode){
			n = new Node(new String(this.getValue()),new ArrayList<Event>(this.getEventList()),
					this.getParent(), new ArrayList<Node>());
			n.setAggregated(false);
			List<Node> children = this.getChildList();
			for (Iterator<Node> iterator = children.iterator(); iterator.hasNext();) {
				Node ch = (Node) iterator.next();
				n.addChild(ch.copy(aggreNode,a));
			}
		}
		else{
			n = new Node(new String(this.getValue()),new ArrayList<Event>(),
					this.getParent(), new ArrayList<Node>());
			n.setActivity(a);
			n.setAggregated(true);
		}
	    return n;
	}
	
	public String treeToString(){
		String res = this.toString();
		res+="\n";
		for(Node n: childList){
			res+=n.treeToString();
		}
		return res;
	}
	
	/**
	 * 
	 * @return a string with all the values unconditionally
	 */
	public String toStringAll(){
		return toStringAll(0);
	}
	public String toStringAll(int level){
		String res = "Node ["+ this.getValue()+
				", isAggregated="+this.isAggreated()+
				", "+this.getActivity()+
				", "+this.getEventList()+
				", parent="+this.getParent()+
				", "+this.getChildList();
		res+="\n";
		for(Node n: childList){
			for(int l=level;l>0;l--)
				res+=" ";
			res+="+-";
			res+="["+level+"] "+n.toStringAll(level+1);
		}
		return res;
	}

	public Node copyWithAggregationLists(int threshold) {
		Node n = new Node(new String(this.getValue()),new ArrayList<Event>(this.getEventList()),
				this.getParent(), new ArrayList<Node>());
		List<Node> children = this.getChildList();
		for (Iterator<Node> iterator = children.iterator(); iterator.hasNext();) {
			Node ch = (Node) iterator.next();
			Activity a = TreeUtils.aggregate(ch, threshold);
			n.setActivity(a);
			n.setAggregated(false);
			n.addChild(ch.copyWithAggregationLists(threshold));
		}
	    return n;
	}

	public Node aggr(int threshold) {
		Node n = new Node(new String(this.getValue()),new ArrayList<Event>(this.getEventList()),
				this.getParent(), new ArrayList<Node>());
		ArrayList<Event> allEvents = new ArrayList<Event>(this.getEventList());
		ArrayList<Event> childsEvents = collectChildsEvents(this);
		allEvents.addAll(childsEvents);
		Activity a = TreeUtils.aggregateFromEventList(allEvents, threshold);
		Activity adjusted = null;
		try {
			adjusted = TreeUtils.adjust(a);
		} catch (CloneNotSupportedException e) {
			adjusted=a;
			e.printStackTrace();
		}
		n.setActivity(adjusted);
		for(Node ch: this.getChildList())
			n.addChild(ch.aggr(threshold));
		return n;
	}

	private static ArrayList<Event> collectChildsEvents(Node node) {
		ArrayList<Event> res = new ArrayList<Event>();
		res.addAll(node.getEventList());
		for(Node ch: node.getChildList())
			res.addAll(collectChildsEvents(ch));
		return res;
	}
	
	

}