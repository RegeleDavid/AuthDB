/*
 * This file is part of AuthDB.
 *
 * Copyright (c) 2011 CraftFire <http://www.craftfire.com/>
 * AuthDB is licensed under the GNU Lesser General Public License.
 *
 * AuthDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AuthDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.craftfire.authdb.scripts.forum;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import com.craftfire.authdb.util.Config;
import com.craftfire.authdb.util.encryption.Encryption;
import com.craftfire.authdb.util.Util;
import com.craftfire.authdb.util.databases.MySQL;

public class SMF {
    public static String Name = "simplemachines";
    public static String ShortName = "smf";
    public static String VersionRange = "1.1.1-1.1.13";
    public static String VersionRange2 = "2.0.0.0-2.0.0.5"; // TODO: Allow use of x.x or x.x.x versions
    public static String LatestVersionRange = VersionRange2;

    public static void adduser(int checkid, String player, String email, String password, String ipAddress) throws SQLException {
        long timestamp = System.currentTimeMillis() / 1000;
        if (checkid == 1) {
            Random r = new Random();
            int randint = r.nextInt(1000000);
            String salt = Encryption.md5("" + randint);
            salt = salt.substring(0, 4);
            String hash = hash(1, player, password);
            int userid;
            PreparedStatement ps;
            ps = MySQL.mysql.prepareStatement("INSERT INTO `" + Config.script_tableprefix + "members" + "` (`memberName`, `dateRegistered`, `lastLogin`, `realName`, `passwd`, `emailAddress`, `memberIP`, `memberIP2`, `lngfile`, `buddy_list`, `pm_ignore_list`, `messageLabels`, `personalText`, `websiteTitle`, `websiteUrl`, `location`, `ICQ`, `MSN`, `signature`, `avatar`, `usertitle`, `secretQuestion`, `additionalGroups`, `passwordSalt`)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 1);
            ps.setString(1, player); // memberName
            ps.setLong(2, timestamp); //d ateRegistered
            ps.setLong(3, timestamp); // lastLogin
            ps.setString(4, player); // realName
            ps.setString(5, hash); // passwd
            ps.setString(6, email); // emailAddress
            ps.setString(7, ipAddress); // memberIP
            ps.setString(8, ipAddress); // memberIP2
            // TODO: Need to add these, it's complaining about default is not set.
            ps.setString(9, ""); // lngfile
            ps.setString(10, ""); // buddy_list
            ps.setString(11, ""); // pm_ignore_list
            ps.setString(12, ""); // messageLabels
            ps.setString(13, ""); // personalText
            ps.setString(14, ""); // websiteTitle
            ps.setString(15, ""); // websiteUrl
            ps.setString(16, ""); // location
            ps.setString(17, ""); // ICQ
            ps.setString(18, ""); // MSN
            ps.setString(19, ""); // signature
            ps.setString(20, ""); // avatar
            ps.setString(21, ""); // usertitle
            ps.setString(22, ""); // secretQuestion
            ps.setString(23, ""); // additionalGroups
            ps.setString(24, salt); // passwordSalt
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();

            userid = MySQL.countitall(Config.script_tableprefix + "members");
            ps = MySQL.mysql.prepareStatement("UPDATE `" + Config.script_tableprefix + "settings" + "` SET `value` = '" + player + "' WHERE `variable` = 'latestRealName'");
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();
            ps = MySQL.mysql.prepareStatement("UPDATE `" + Config.script_tableprefix + "settings" + "` SET `value` = '" + userid + "' WHERE `variable` = 'latestMember'");
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();
            ps = MySQL.mysql.prepareStatement("UPDATE `" + Config.script_tableprefix + "settings" + "` SET `value` = '" + timestamp + "' WHERE `variable` = 'memberlist_updated'");
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();
            ps = MySQL.mysql.prepareStatement("UPDATE `" + Config.script_tableprefix + "settings" + "` SET `value` = value + 1 WHERE `variable` = 'totalMembers'");
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();
        } else if (checkid == 2) {
            Random r = new Random();
            int randint = r.nextInt(1000000);
            String salt = Encryption.md5("" + randint);
            salt = salt.substring(0, 4);
            String hash = hash(2, player, password);
            int userid;
            PreparedStatement ps;
            ps = MySQL.mysql.prepareStatement("INSERT INTO `" + Config.script_tableprefix + "members" + "` (`member_name`, `date_registered`, `last_login`, `real_name`, `passwd`, `email_address`, `member_ip`, `member_ip2`, `lngfile`, `buddy_list`, `pm_ignore_list`, `message_labels`, `personal_text`, `website_title`, `website_url`, `location`, `icq`, `msn`, `signature`, `avatar`, `usertitle`, `secret_question`, `additional_groups`, `openid_uri`, `ignore_boards`, `password_salt`)  VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 1);
            ps.setString(1, player); // member_name
            ps.setLong(2, timestamp); // date_registered
            ps.setLong(3, timestamp); // last_login
            ps.setString(4, player); // real_name
            ps.setString(5, hash); // passwd
            ps.setString(6, email); // email_address
            ps.setString(7, ipAddress); // memberIP
            ps.setString(8, ipAddress); // memberIP2
            // TODO: Need to add these, it's complaining about default is not set.
            ps.setString(9, ""); // lngfile
            ps.setString(10, ""); // buddy_list
            ps.setString(11, ""); // pm_ignore_list
            ps.setString(12, ""); // message_labels
            ps.setString(13, ""); // personal_text
            ps.setString(14, ""); // website_title
            ps.setString(15, ""); // website_url
            ps.setString(16, ""); // location
            ps.setString(17, ""); // ICQ
            ps.setString(18, ""); // MSN
            ps.setString(19, ""); // signature
            ps.setString(20, ""); // avatar
            ps.setString(21, ""); // usertitle
            ps.setString(22, ""); // secret_question
            ps.setString(23, ""); // additional_groups
            ps.setString(24, ""); // openid_uri
            ps.setString(25, ""); // ignore_boards
            ps.setString(26, salt); // password_salt
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();

            userid = MySQL.countitall(Config.script_tableprefix + "members");
            ps = MySQL.mysql.prepareStatement("UPDATE `" + Config.script_tableprefix + "settings" + "` SET `value` = '" + player + "' WHERE `variable` = 'latestRealName'");
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();
            ps = MySQL.mysql.prepareStatement("UPDATE `" + Config.script_tableprefix + "settings" + "` SET `value` = '" + userid + "' WHERE `variable` = 'latestMember'");
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();
            ps = MySQL.mysql.prepareStatement("UPDATE `" + Config.script_tableprefix + "settings" + "` SET `value` = '" + timestamp + "' WHERE `variable` = 'memberlist_updated'");
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();
            ps = MySQL.mysql.prepareStatement("UPDATE `" + Config.script_tableprefix + "settings" + "` SET `value` = value + 1 WHERE `variable` = 'totalMembers'");
            Util.logging.mySQL(ps.toString());
            ps.executeUpdate();
            ps.close();
        }
    }

    public static String hash(int checkid, String player, String password) {
        if (checkid == 1) {
            try {
                String temp = player.toLowerCase() + password;
                return Encryption.SHA1(temp);
            } catch (NoSuchAlgorithmException e) {
                Util.logging.StackTrace(e.getStackTrace(), Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), Thread.currentThread().getStackTrace()[1].getClassName(), Thread.currentThread().getStackTrace()[1].getFileName());
            } catch (UnsupportedEncodingException e) {
                Util.logging.StackTrace(e.getStackTrace(), Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), Thread.currentThread().getStackTrace()[1].getClassName(), Thread.currentThread().getStackTrace()[1].getFileName());
            }
        } else if (checkid == 2) {
            try {
                String temp = player.toLowerCase() + password;
                return Encryption.SHA1(temp);
            } catch (NoSuchAlgorithmException e) {
                Util.logging.StackTrace(e.getStackTrace(), Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), Thread.currentThread().getStackTrace()[1].getClassName(), Thread.currentThread().getStackTrace()[1].getFileName());
            } catch (UnsupportedEncodingException e) {
                Util.logging.StackTrace(e.getStackTrace(), Thread.currentThread().getStackTrace()[1].getMethodName(), Thread.currentThread().getStackTrace()[1].getLineNumber(), Thread.currentThread().getStackTrace()[1].getClassName(), Thread.currentThread().getStackTrace()[1].getFileName());
            }
        }
        return "fail";
    }

    public static boolean check_hash(String passwordhash, String hash) {
        if (passwordhash.equals(hash)) {
            return true;
        } else {
            return false;
        }
    }
}
