package connections.service;

import java.util.HashMap;
import java.util.LinkedList;

import connections.dto.Connection;

public class ConnectionsPoolLLImpl implements ConnectionsPool  {
	
	HashMap<Integer, Connection> mapConnections = new HashMap<>();
	LinkedList<Connection> list = new LinkedList<>();
	int poolMaxSize;
	
	public ConnectionsPoolLLImpl(int maxSize) {
		poolMaxSize = maxSize;
	}

	@Override
	public boolean addConnection(Connection connection) {
		if(mapConnections.containsKey(connection.getId())) {
			return false;
		}
		if(list.size() == poolMaxSize) {
			int idToBeRemoved = list.getLast().getId();
			list.removeLast();
			mapConnections.remove(idToBeRemoved);
		}
		list.addFirst(connection);
		mapConnections.put(connection.getId(), connection);
		return true;
	}

	@Override
	public Connection getConnection(int id) {
		Connection connection = mapConnections.get(id);
		if (connection == null) {
			return null;
		}
		list.remove(connection);
		list.addFirst(connection);
		return connection;
	}

	public int getNewestConnectionId() {
		if(list.size()==0) {
			return -5;
		}
		return list.getFirst().getId();
	}
}
