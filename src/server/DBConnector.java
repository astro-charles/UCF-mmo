package server;

import java.sql.*;
import java.util.*;

public class DBConnector {
	private static final boolean DEBUG = true;
    private static final String DEFAULT_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DEFAULT_URL = "jdbc:mysql://mmo.cg8eat1oqjuj.us-east-1.rds.amazonaws.com:3306/mmo";
    private static final String DEFAULT_USERNAME = "mmo_admin";
    private static final String DEFAULT_PASSWORD = "1234567aA";
//
//    public static void main(String[] args) {
//        long begTime = System.currentTimeMillis();
//
//        try {
//        	updateUser("johndoe", 41.4, 50.2, 10, 100, 100, 500);
//		} catch (ClassNotFoundException | SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//        long endTime = System.currentTimeMillis();
//        System.out.println("wall time: " + (endTime - begTime) + " ms");
//       
//    }
    public DBConnector() {
    	
    }

    public static Connection createConnection(String driver, String url, String username, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        if ((username == null) || (password == null) || (username.trim().length() == 0) || (password.trim().length() == 0)) {
            return DriverManager.getConnection(url);
        } else {
            return DriverManager.getConnection(url, username, password);
        }
    }
    /**
     * Call this when first time user connects
     * @param username name of the new user
     * @param pos_x choose a default x location
     * @param pos_y choose a default y location
     * @throws ClassNotFoundException 
     * @throws SQLException
     */
    public static void newUser(String username, double pos_x, double pos_y) throws ClassNotFoundException, SQLException {
    	Connection connection = null;
    	try {
	    	connection = createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	    	connection.setAutoCommit(false);
	    	String sqlUpdate = "INSERT INTO creature_db(username, pos_x, pos_y, level, hitpoint_curr, hitpoints_max, experience) VALUES(?,?,?,?,?,?,?)";
	    	List parameters = Arrays.asList(username, pos_x,pos_y,0,100,100,0);
	    	update(connection, sqlUpdate, parameters);
	    	connection.commit();
	    	if(DEBUG) {
		    	String sqlQuery = "SELECT * FROM creature_db";
		    	System.out.println("new insert: " + query(connection, sqlQuery, Collections.emptyList()));
	    	}
    	}catch(Exception e) {
    		connection.rollback();
    		e.printStackTrace();
    	}finally {
    		close(connection);
    	}
    }
    /**
     * Call this when user logsout
     * @param username name of the new user
     * @param pos_x current x location
     * @param pos_y current y location
     * @param level current level
     * @param etc
     * @throws ClassNotFoundException 
     * @throws SQLException
     */
    public static void updateUser(String username, double pos_x, double pos_y, int level, int hitpoint_curr, int hitpoints_max, int experience) throws ClassNotFoundException, SQLException {
    	Connection connection = null;
    	try {
	    	connection = createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
			connection.setAutoCommit(false);
			//update creature_db SET pos_x = 10.5, pos_y =  .435 WHERE username like "johndoe";

			String sqlUpdate = "update creature_db SET pos_x=" + pos_x + ",pos_y=" + pos_y + ",level=" + level + ",hitpoint_curr=" + hitpoint_curr + ",hitpoints_max=" + hitpoints_max + ",experience=" + experience + " WHERE username LIKE \"" + username + "\"";
//			List parameters = Arrays.asList(username, pos_x,pos_y,level,hitpoint_curr,hitpoints_max,experience);
			update(connection, sqlUpdate, Collections.emptyList());
			connection.commit();
			if(DEBUG) {  
				String sqlQuery = "SELECT * FROM creature_db";
				System.out.println("after update: " + query(connection, sqlQuery, Collections.emptyList()));
			}
    	}catch(Exception e) {
    		connection.rollback();
    		e.printStackTrace();
    	}finally {
    		close(connection);
    	}
    }
    
    public static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void close(Statement st) {
        try {
            if (st != null) {
                st.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Map<String, Object>> map(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
        try {
            if (rs != null) {
                ResultSetMetaData meta = rs.getMetaData();
                int numColumns = meta.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<String, Object>();
                    for (int i = 1; i <= numColumns; ++i) {
                        String name = meta.getColumnName(i);
                        Object value = rs.getObject(i);
                        row.put(name, value);
                    }
                    results.add(row);
                }
            }
        } finally {
            close(rs);
        }
        return results;
    }

    public static List<Map<String, Object>> query(Connection connection, String sql, List<Object> parameters) throws SQLException {
        List<Map<String, Object>> results = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);

            int i = 0;
            for (Object parameter : parameters) {
                ps.setObject(++i, parameter);
            }
            rs = ps.executeQuery();
            results = map(rs);
        } finally {
            close(rs);
            close(ps);
        }
        return results;
    }

    public static int update(Connection connection, String sql, List<Object> parameters) throws SQLException {
        int numRowsUpdated = 0;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);

            int i = 0;
            for (Object parameter : parameters) {
                ps.setObject(++i, parameter);
            }
            numRowsUpdated = ps.executeUpdate();
        } finally {
            close(ps);
        }
        return numRowsUpdated;
    }
    
    public static List<Map<String, Object>> getUser(String username) { 
    	Connection connection = null;
    	List<Map<String, Object>> result = null;
    	try {
	    	connection = createConnection(DEFAULT_DRIVER, DEFAULT_URL, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	    	String sqlQuery = "SELECT * FROM creature_db WHERE username LIKE \""+username+"\"";
	    	result = query(connection, sqlQuery, Collections.emptyList());
    	}catch(Exception e) {
    		e.printStackTrace();
    	}finally {
    		close(connection);
    	}
    	return result;
    }
}