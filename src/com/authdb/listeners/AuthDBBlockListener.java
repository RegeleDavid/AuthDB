/**          (C) Copyright 2011 Contex <contexmoh@gmail.com>
	
This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License. 
To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/3.0/ 
or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.

**/
package com.authdb.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockInteractEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.authdb.AuthDB;


public class AuthDBBlockListener extends BlockListener
{
  private final AuthDB plugin;

  public AuthDBBlockListener(AuthDB instance)
  {
	  plugin = instance;
  }

  public void onBlockPlace(BlockPlaceEvent event) {
    if (!AuthDB.isAuthorized(event.getPlayer().getEntityId()))
      event.setCancelled(true);
  }

  public void onBlockDamage(BlockDamageEvent event) {
   if (!AuthDB.isAuthorized(event.getPlayer().getEntityId()))
      event.setCancelled(true);
  }
  
  public void onBlockInteract(BlockInteractEvent event) {
    LivingEntity e = event.getEntity();
    if ((e != null) && ((e instanceof HumanEntity)) && 
      (!AuthDB.isAuthorized(e.getEntityId())))
      event.setCancelled(true);
  }
}
