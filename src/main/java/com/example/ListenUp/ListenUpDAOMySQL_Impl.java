//package com.example.ListenUp;
//
//
//import java.sql.Connection;
//	import java.sql.DriverManager;
//	import java.sql.PreparedStatement;
//	import java.sql.ResultSet;
//	import java.sql.SQLException;
//	import java.sql.Statement;
//	import java.util.ArrayList;
//	import java.util.List;
//
//	public class ListenUpDAOMySQL_Impl implements ListenUpDAO
//	{
//
//		public static final String CONNECTION_URL = "jdbc:mysql://localhost/listenup_db";
//
//		@Override
//		public void addSongs(ListenUp lis)
//		{
//			try
//			{
//				Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement("insert into songs(ArtistBand, Title, Duration, Type, URL) values(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
//				ps.setString(1, lis.getArtistBand());
//				ps.setString(2, lis.getTitle());
//				ps.setString(3, lis.getDuration());
//				ps.setString(4, lis.getType());
//				ps.setString(5, lis.getURL());
//				int affectedRows = ps.executeUpdate();
//				ResultSet rs = ps.getGeneratedKeys();
//				if(rs.next())
//				{
//					lis.setId(rs.getInt(1));
//				}
//				closeConnection(conn);
//			}
//			catch(SQLException e)
//			{
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		public boolean update(ListenUp lis)
//		{
//			try
//			{
//				Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement("update songs set title = ? where id = ?");
//				ps.setString(1, lis.getTitle());
//				ps.setInt(2, lis.getId());
//				int affectedRows = ps.executeUpdate();
//				closeConnection(conn);
//				return affectedRows == 1;
//			}
//			catch(SQLException e)
//			{
//				return false;
//			}
//		}
//
//		@Override
//		public boolean delete(int id)
//		{
//			try
//			{
//				Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement("delete from songs where id = ?");
//				ps.setInt(1, id);
//				int affectedRows = ps.executeUpdate();
//				closeConnection(conn);
//				return affectedRows == 1;
//			}
//			catch(SQLException e)
//			{
//				return false;
//			}
//		}
//
//		@Override
//		public ListenUp findById(int id)
//		{
//			ListenUp lis = null;
//			try
//			{
//				Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement("select * from songs where id = ?");
//				ps.setInt(1, id);
//				ResultSet rs = ps.executeQuery();
//				if(rs.next())
//				{
//					int id2 = rs.getInt("id");
////					String nume= rs.getString("nume");
//					lis = new ListenUp(id2);
//				}
//				closeConnection(conn);
//				return lis;
//			}
//			catch(SQLException e)
//			{
//				return null;
//			}
//		}
//
//		@Override
//		public ListenUp[] getAllSongs()
//		{
//			try
//			{
//				Connection conn = getConnection();
//				PreparedStatement ps = conn.prepareStatement("select * from songs");
//				ResultSet rs = ps.executeQuery();
//				List<ListenUp> listenUpList = new ArrayList<ListenUp>();
//				while(rs.next())
//				{
//					int id = rs.getInt("id");
//					String ArtistBand= rs.getString("ArtistBand");
//					String Title= rs.getString("Title");
//					String Duration= rs.getString("Duration");
//					String Type= rs.getString("Type");
//					String URL= rs.getString("URL");
//					listenUpList.add(new ListenUp(id,ArtistBand,Title,Duration,Type,URL));
//				}
//				closeConnection(conn);
//				return listenUpList.toArray(new ListenUp[listenUpList.size()]);
//			}
//			catch(SQLException e)
//			{
//				return null;
//			}
//		}
//
//
//
//		public Connection getConnection() throws SQLException
//		{
//			return DriverManager.getConnection(CONNECTION_URL, "root", "strongpassword99!");
//		}
//
//		public void closeConnection(Connection conn) throws SQLException
//		{
//			conn.close();
//		}
//	}
//
