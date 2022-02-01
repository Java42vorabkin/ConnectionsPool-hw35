package connections.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import connections.dto.Connection;

class ConnectionPoolTests {
private static final int LIMIT = 5;
private static final int ID1 = 1;
private static final int ID2 = 2;
private static final int ID3 = 3;
private static final int ID4 = 4;
private static final int ID5 = 5;
private static final int ID101 = 101;
private static final int ID103 = 103;
private static String IP_ADRESS = "192.192.192.192";
private static int PORT = 1234;
Connection conn1 = new Connection(ID1, IP_ADRESS, PORT);
Connection conn2 = new Connection(ID2, IP_ADRESS, PORT);
Connection conn3 = new Connection(ID3, IP_ADRESS, PORT);
Connection conn4 = new Connection(ID4, IP_ADRESS, PORT);
Connection conn5 = new Connection(ID5, IP_ADRESS, PORT);
Connection conn101 = new Connection(ID101, IP_ADRESS, PORT);
Connection conn103 = new Connection(ID103, IP_ADRESS, PORT);


ConnectionsPool pool = new ConnectionsPoolImpl(LIMIT);
ConnectionsPoolImpl poolImpl = (ConnectionsPoolImpl)pool;
//ConnectionsPool pool = new ConnectionsPoolLLImpl(LIMIT);
//ConnectionsPoolLLImpl poolImpl = (ConnectionsPoolLLImpl)pool;

	@BeforeEach
	void setUp() throws Exception {
		pool.addConnection(conn1);
		pool.addConnection(conn2);
		pool.addConnection(conn3);
		pool.addConnection(conn4);
		pool.addConnection(conn5);		
	}

	@Test
	void testAddConnection() {
		assertEquals(ID5, poolImpl.getNewestConnectionId());
		assertFalse(pool.addConnection(conn1));
		assertTrue(pool.addConnection(new Connection(ID101, IP_ADRESS, PORT)));
		assertEquals(ID101, poolImpl.getNewestConnectionId());
	}

	@Test
	void testGetConnection() {
		pool.addConnection(new Connection(ID101, IP_ADRESS, PORT));
		assertNull(pool.getConnection(ID1));
		assertEquals(ID101, poolImpl.getNewestConnectionId());
		assertEquals(conn2, pool.getConnection(ID2));
		assertEquals(ID2, poolImpl.getNewestConnectionId());
		Connection connection = new Connection(ID103, IP_ADRESS, PORT);
		pool.addConnection(connection);
		assertEquals(ID103, poolImpl.getNewestConnectionId());
		assertNull(pool.getConnection(ID3));
		assertEquals(connection, pool.getConnection(ID103));
		assertEquals(conn5, pool.getConnection(ID5));
		assertEquals(ID5, poolImpl.getNewestConnectionId());
	}

}
