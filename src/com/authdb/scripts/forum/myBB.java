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
import java.util.Arrays;

import com.authdb.util.Config;
import com.authdb.util.Encryption;
import com.authdb.util.Util;
import com.authdb.util.databases.MySQL;


  public class myBB {
	  
		public static boolean check()
		{
			String name = Config.Script3_name;
			String latest = Config.Script3_latest;
			String[] versions = new String[] {Config.Script3_versions};
			String Version = Util.CheckVersion(name,latest, 3);
			if(Arrays.asList(versions).contains(Version))
			{
				if(Config.debug_enable) Util.Debug("Version: "+Version+" is in the list of supported versions of this script ("+name+")");
				return true;
			}
			else 
			{ 
				Util.Log("warning","Version: "+Version+" is NOT in the list of supported versions of this script ("+name+") Setting to latest version of script: "+name+" "+latest); 
				Config.script_version = latest;
				return true;
			}
		}
  	
    public static void adduser(String player, String email, String password, String ipAddress) throws SQLException
    {
		if(check())
	    {
	long timestamp = System.currentTimeMillis()/1000;
	String salt = Encryption.hash(8,"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",0,0);
	String hash = hash("create",player,password, salt);
	///int userid;
	//
	PreparedStatement ps;
	//
	ps = MySQL.mysql.prepareStatement("INSERT INTO `"+Config.database_prefix+"users"+"` (`username`,`password`,`salt`,`email`,`regdate`,`lastactive`,`lastvisit`,`regip`,`longregip`,`signature`,`buddylist`,`ignorelist`,`pmfolders`,`notepad`,`usernotes`,`usergroup`)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 1);
	ps.setString(1, player); //username
	ps.setString(2, hash); // password
	ps.setString(3, salt); //salt
	ps.setString(4, email); //email
	ps.setLong(5, timestamp); //regdate
	ps.setLong(6, timestamp); //lastactive
	ps.setLong(7, timestamp); //lastvisit
	ps.setString(8, ipAddress); //regip
	//ps.setLong(9, Util.IP2Long(ipAddress)); //longregip
	ps.setString(9, "2130706433");
	//need to add these, it's complaining about not default is set.
	ps.setString(10, ""); //signature
	ps.setString(11, ""); //buddylist
	ps.setString(12, ""); //ignorelist
	ps.setString(13, ""); //pmfolders
	ps.setString(14, ""); //notepad
	ps.setString(15, ""); //usernotes
	ps.setString(16, "5");//usergroup
	    ps.executeUpdate();
	 
	    /*userid = MySQL.countitall(Config.database_prefix+"users");
	    String oldcache =  MySQL.getfromtable(Config.database_prefix+"datacache", "`cache`", "title", "stats");
	    StringTokenizer st = new StringTokenizer(oldcache,":");
	    int i = 0, usernamelength = player.length();
	    String numusers, lastuid, lastusername, totalusers = "", newcache = "";
	    while (st.hasMoreTokens()) {
	   if(i == 5) 
	    	{ 
	    		st.nextToken();
	    		newcache += usernamelength+":";
	    	}
	    	else if(i == 6) 
	    	{ 
	    		numusers = st.nextToken();
	    		numusers = Util.removeChar(numusers,'"');
				numusers = Util.removeChar(numusers,'s');
				numusers = Util.removeChar(numusers,';');
				numusers = numusers.trim();
				int numuserNumber = Integer.parseInt(numusers) + 1;
				totalusers += numuserNumber;
				numusers = "\""+numuserNumber+"\""+";s";
				newcache += numusers+":";
	    	}
	    	else if(i == 17) 
	    	{ 
	    		String dupe = "";
	    		dupe += userid;
	    		st.nextToken();
	    		newcache += dupe.length()+":";
	    	}
	    	else if(i == 21) 
	    	{ 
	    		 st.nextToken();
	    		lastuid = "\""+userid+"\";}";
	    		newcache += lastuid;
	    	}
	    	else if(i == 24) 
	    	{ 
	    		st.nextToken();
	    		newcache += usernamelength+":";
	    	}
	    	else if(i == 25) 
	    	{ 
	    		st.nextToken();
	    		lastusername = "\""+player+"\";s";
	    		newcache += lastusername+":";
	    	}
	      else
	      {  
	    	  newcache += st.nextToken()+":"; 
	      }
	     Util.Log("info",i+"-"+st.nextToken()+":"); 
	      i++;
	    }
	  StringTokenizer st2 = new StringTokenizer(newcache,":");
	    String newcache2 = "";
	    while (st2.hasMoreTokens()) {
	    	if(i == 5) 
	    	{ 
	    		st.nextToken();
	    		newcache2 += totalusers.length()+":";
	    	}
	        else
	        {  
	      	  newcache2 += st.nextToken()+":"; 
	        }
	    }
	      //ps = MySQL.mysql.prepareStatement("UPDATE `"+Config.database_prefix+"datacache"+"` SET `cache` = '" + newcache2 + "' WHERE `title` = 'stats'");
	     // ps.executeUpdate();
	      */
	    }
    }
  	
    public static String hash(String action,String player,String password, String thesalt) throws SQLException {
    	if(action.equals("find"))
    	{
  	try {
  		String salt = MySQL.getfromtable(Config.database_prefix+"users", "`salt`", "username", player);
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
  	return Encryption.md5(Encryption.md5(salt) + Encryption.md5(password));
  	}
}