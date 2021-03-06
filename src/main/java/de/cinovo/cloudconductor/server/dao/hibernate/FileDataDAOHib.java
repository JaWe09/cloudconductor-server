package de.cinovo.cloudconductor.server.dao.hibernate;

/*
 * #%L
 * cloudconductor-server
 * %%
 * Copyright (C) 2013 - 2014 Cinovo AG
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * #L%
 */

import org.springframework.stereotype.Repository;

import de.cinovo.cloudconductor.server.dao.IFileDataDAO;
import de.cinovo.cloudconductor.server.model.EFile;
import de.cinovo.cloudconductor.server.model.EFileData;
import de.cinovo.cloudconductor.server.model.enums.AuditCategory;

/**
 * Copyright 2013 Cinovo AG<br>
 * <br>
 * 
 * @author psigloch
 * 
 */
@Repository("FileDataDAOHib")
public class FileDataDAOHib extends AVersionedEntityHib<EFileData> implements IFileDataDAO {
	
	@Override
	public Class<EFileData> getEntityClass() {
		return EFileData.class;
	}
	
	@Override
	protected AuditCategory getAuditCategory() {
		return AuditCategory.FILE_DATA;
	}
	
	@Override
	public EFileData findDataByFile(EFile file) {
		String query = "FROM EFileData f WHERE f.parent.id = ?1 OR f.origId = ?2 ";
		return this.findVersionedByQuery(query, "f", file.getId(), file.getOrigId());
	}
}
