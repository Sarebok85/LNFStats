package net.ddns.jmsola.customproject.dao.repositories;

import net.ddns.jmsola.customproject.dao.commons.api.Dao;
import net.ddns.jmsola.customproject.model.entities.User;
import net.ddns.jmsola.customproject.model.filters.UserSearchFilter;

public interface UserDao extends Dao<User, UserSearchFilter, Integer> {
	
	User findById(Integer Id);
	
}
