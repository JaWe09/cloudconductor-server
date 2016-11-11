package de.cinovo.cloudconductor.server.dao.hibernate;

import org.springframework.stereotype.Repository;

import de.cinovo.cloudconductor.server.dao.IAgentDAO;
import de.cinovo.cloudconductor.server.model.EAgent;
import de.taimos.dao.hibernate.EntityDAOHibernate;

/**
 * Copyright 2016 Cinovo AG<br>
 * <br>
 * 
 * @author ablehm
 * 
 */
@Repository("AgentDAOHib")
public class AgentDAOHib extends EntityDAOHibernate<EAgent, Long> implements IAgentDAO {
	
	@Override
	public Class<EAgent> getEntityClass() {
		return EAgent.class;
	}
	
}