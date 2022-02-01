package connections.service;

import java.util.HashMap;
import java.util.LinkedList;

import connections.dto.Connection;

public class ConnectionsPoolImpl implements ConnectionsPool {
	private static class Node {
		Connection connection;
		Node prev;
		Node next;
		public Node(Connection connection) {
			this.connection = connection;
		}
	}
	private static class ConnectionsList {
		Node head = null;
		Node tail = null;
		
		public void addConnection(Connection connection) {
			Node newNode = new Node(connection);
			if (head == null) {
				head = tail = newNode;
			} else {
				addNodeAsHead(newNode);
			}
		}

		public void moveNodeToHead(Node node) {
			if (node != tail) {
				// remove not tail node
				removeNode(node);
			} else {
				// remove tail node
				removeLastNode();
			}
			// move node to the head
			addNodeAsHead(node);
		}

		private void addNodeAsHead(Node node) {
			node.prev = null;
			node.next = head;
			head.prev = node;
			head = node;
		}

		private void removeLastNode() {
			tail = tail.prev;
			tail.next = null;
		}

		private void removeNode(Node node) {
			if(head.connection.getId() != node.connection.getId()) {
				Node previous = node.prev;
				Node next = node.next;
				previous.next = next;
				next.prev = previous;
			}
		}
	}
	ConnectionsList list = new ConnectionsList();
	HashMap<Integer, Node> mapConnections = new HashMap<>();
	int poolMaxSize;
	public ConnectionsPoolImpl(int maxSize) {
		this.poolMaxSize = maxSize;
	}

	@Override
	public boolean addConnection(Connection connection) {
		if (mapConnections.containsKey(connection.getId())) {
			return false;
		}
		if (mapConnections.size() == poolMaxSize) {
			int idToBeRemoved = list.tail.connection.getId();
			list.removeLastNode();
			mapConnections.remove(idToBeRemoved);
		}
		list.addConnection(connection);
		mapConnections.put(connection.getId(), list.head);
		return true;
	}

	@Override
	public Connection getConnection(int id) {
		Node nodeById = mapConnections.get(id);
		if (nodeById == null) {
			return null;
		}
		if (nodeById.prev != null) {
			list.moveNodeToHead(nodeById);
		}
		return nodeById.connection;
	}
	
	public int getNewestConnectionId() {
		if(list.head==null) {
			return -5;
		}
		return list.head.connection.getId();
	}
}
