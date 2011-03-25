/**          (C) Copyright 2011 Contex <contexmoh@gmail.com>
	
	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/
package com.authdb.scripts.forum;

  import java.io.UnsupportedEncodingException;
  import java.security.NoSuchAlgorithmException;
  import java.sql.PreparedStatement;
  import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.authdb.util.Config;
import com.authdb.util.Encryption;
import com.authdb.util.Util;
import com.authdb.util.databases.MySQL;


  public class vB {
	  
    public static void adduser(int checkid, String player, String email, String password, String ipAddress) throws SQLException
    {
  	long timestamp = System.currentTimeMillis()/1000;
	  	if(checkid == 1)
	  	{
		  	String salt = Encryption.hash(30,"none",33,126);
		  	String passwordhashed = hash("create",player,password, salt);
		  	String passworddate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date (timestamp*1000));
		  	///
		  	PreparedStatement ps;
		  	//
		  	ps = MySQL.mysql.prepareStatement("INSERT INTO `"+Config.database_prefix+"user"+"` (`usergroupid`,`password`,`passworddate`,`email`,`showvbcode`,`joindate`,`lastvisit`,`lastactivity`,`reputationlevelid`,`options`,`ipaddress`,`salt`,`username`)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
		    ps.setString(1, "2"); //usergroupid
		  	ps.setString(2, passwordhashed); // password
		    ps.setString(3, passworddate); //passworddate
		    ps.setString(4, email); //email
		  	ps.setString(5, "1"); //showvbcode
		  	ps.setLong(6, timestamp); //joindate
		  	ps.setLong(7, timestamp); //lastvisit
		  	ps.setLong(8, timestamp); //lastactivity
		  	ps.setString(9, "5"); //reputationlevelid
			ps.setString(10, "45108311"); //options
			ps.setLong(11, Util.IP2Long(ipAddress)); //ipaddress
			ps.setString(12, salt); //salt
			ps.setString(13, player); //username
		    ps.executeUpdate();
			
		    
		    int userid = MySQL.countitall(Config.database_prefix+"user");
		    String oldcache =  MySQL.getfromtable(Config.database_prefix+"datastore", "`data`", "title", "userstats");
		    String newcache = Util.ForumCache(oldcache, player, userid, "numbermembers", "activemembers", "newusername", "newuserid");
		    ps = MySQL.mysql.prepareStatement("UPDATE `"+Config.database_prefix+"datastore"+"` SET `data` = '" + newcache + "' WHERE `title` = 'userstats'");
		    ps.executeUpdate();
		      
	  		}
	  	else if(checkid == 2)
	  	{
		  	String salt = Encryption.hash(30,"none",33,126);
		  	String passwordhashed = hash("create",player,password, salt);
		  	String passworddate = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date (timestamp*1000));
		  //	int userid;
		  	///
		  	PreparedStatement ps;
		  	//
		  	ps = MySQL.mysql.prepareStatement("INSERT INTO `"+Config.database_prefix+"user"+"` (`usergroupid`,`password`,`passworddate`,`email`,`showvbcode`,`joindate`,`lastvisit`,`lastactivity`,`reputationlevelid`,`options`,`ipaddress`,`salt`,`username`,`usertitle`)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
		    ps.setString(1, "2"); //usergroupid
		  	ps.setString(2, passwordhashed); // password
		    ps.setString(3, passworddate); //passworddate
		    ps.setString(4, email); //email
		  	ps.setString(5, "1"); //showvbcode
		  	ps.setLong(6, timestamp); //joindate
		  	ps.setLong(7, timestamp); //lastvisit
		  	ps.setLong(8, timestamp); //lastactivity
		  	ps.setString(9, "5"); //reputationlevelid
			ps.setString(10, "45091927"); //options
			ps.setString(11, ipAddress); //ipaddress
			ps.setString(12, salt); //salt
			ps.setString(13, player); //username
			ps.setString(14, "Junior Member"); //usertitle
		    ps.executeUpdate();
		     
		    int userid = MySQL.countitall(Config.database_prefix+"user");
		    String oldcache =  MySQL.getfromtable(Config.database_prefix+"datastore", "`data`", "title", "userstats");
		    String newcache = Util.ForumCache(oldcache, player, userid, "numbermembers", "activemembers", "newusername", "newuserid");
		    ps = MySQL.mysql.prepareStatement("UPDATE `"+Config.database_prefix+"datastore"+"` SET `data` = '" + newcache + "' WHERE `title` = 'userstats'");
		    ps.executeUpdate();
	  	}
    }
    
    public static String hash(String action,String player,String password, String thesalt) throws SQLException {
    	if(action.equals("find"))
    	{
  	try {
  		String salt = MySQL.getfromtable(Config.database_prefix+"user", "`salt`", "username", player);
  		return passwordHash(password, salt);
  	} catch (NoSuchAlgorithmException e) {
  		e.printStackTrace();
  	} catch (UnsupportedEncodingException e) {
  		e.printStackTrace();
  	}
    	}
    	else if(action.equals("create"))
    	{
    		try {
				return passwordHash(password, thesalt);
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
  	return "fail";
    }

  	public static boolean check_hash(String passwordhash, String hash)
  	{
  		if(passwordhash.equals(hash)) return true;
  		else return false;
  	}
  	
  	public static String passwordHash(String password, String salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
  	{
  	return Encryption.md5(Encryption.md5(password)+salt);
  	}
}