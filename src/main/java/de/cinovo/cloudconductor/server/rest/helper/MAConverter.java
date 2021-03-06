package de.cinovo.cloudconductor.server.rest.helper;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.cinovo.cloudconductor.api.ServiceState;
import de.cinovo.cloudconductor.api.model.*;
import de.cinovo.cloudconductor.api.model.Package;
import de.cinovo.cloudconductor.server.model.*;

/**
 * Copyright 2013 Cinovo AG<br>
 * <br>
 * 
 * @author psigloch
 * 
 */
public class MAConverter {
	
	private MAConverter() {
		// nothing to do, but prevent init;
	}
	
	/**
	 * @param <K> the model object
	 * @param models set of model objects
	 * @return set of api objects
	 */
	public static <K extends INamed> Set<String> fromModel(Collection<K> models) {
		Set<String> result = new HashSet<>();
		for (K model : models) {
			if (!model.getName().isEmpty()) {
				result.add(model.getName());
			}
		}
		return result;
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static Template fromModel(ETemplate model) {
		Map<String, String> rpms = new HashMap<>();
		for (EPackageVersion rpm : model.getPackageVersions()) {
			rpms.put(rpm.getPkg().getName(), rpm.getVersion());
		}
		return new Template(model.getName(), model.getDescription(), model.getYumPath(), rpms, //
		MAConverter.fromModel(model.getHosts()), MAConverter.fromModel(model.getSshkeys()), //
		MAConverter.fromModel(model.getConfigFiles()), //
				MAConverter.fromModel(model.getDirectory()));
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static SSHKey fromModel(ESSHKey model) {
		return new SSHKey(model.getKeycontent(), model.getOwner());
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static Service fromModel(EService model) {
		return new Service(model.getName(), model.getDescription(), model.getInitScript(), ServiceState.STOPPED, MAConverter.fromModel(model.getPackages()));
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static Service fromModel(EServiceState model) {
		return new Service(model.getService().getName(), model.getService().getDescription(), model.getService().getInitScript(), model.getState(), MAConverter.fromModel(model.getService().getPackages()));
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static ConfigFile fromModel(EFile model) {
		Set<String> services = MAConverter.fromModel(model.getDependentServices());
		return new ConfigFile(model.getName(), model.getPkg() == null ? null : model.getPkg().getName(), model.getTargetPath(), //
		model.getOwner(), model.getGroup(), model.getFileMode(), model.isTemplate(), model.isReloadable(), //
		model.getChecksum(), services);
	}

	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static Directory fromModel(EDirectory model)
	{
		Set<String> services = MAConverter.fromModel(model.getDependentServices());
		return new Directory(model.getName(), model.getPkg() == null ? null : model.getPkg().getName(), model.getTargetPath(), //
				model.getOwner(), model.getGroup(), model.getFileMode(), services);
	}

	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static Dependency fromModel(EDependency model) {
		return new Dependency(model.getName(), model.getVersion(), model.getOperator(), model.getType().toString());
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static Host fromModel(EHost model) {
		Set<String> services = new HashSet<>();
		for (EServiceState ss : model.getServices()) {
			if (!ss.getService().getName().isEmpty()) {
				services.add(ss.getService().getName());
			}
		}
		return new Host(model.getName(), model.getDescription(), model.getTemplate().getName(), services, model.getUuid());
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static Package fromModel(EPackage model) {
		Set<String> rpms = MAConverter.fromModel(model.getRPMs());
		return new Package(model.getName(), model.getDescription(), rpms);
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static PackageVersion fromModel(EPackageVersion model) {
		Set<Dependency> deps = new HashSet<>();
		for (EDependency md : model.getDependencies()) {
			deps.add(MAConverter.fromModel(md));
		}
		return new PackageVersion(model.getPkg().getName(), model.getVersion(), deps);
	}
	
	/**
	 * @param model the model object
	 * @return the api object
	 */
	public static AgentOptions fromModel(EAgentOption model) {
		return new AgentOptions(model.getAliveTimer(), model.getAliveTimerUnit(), model.getDoSshKeys(), model.getSshKeysTimer(), model.getSshKeysTimerUnit(), model.getDoPackageManagement(), model.getPackageManagementTimer(), model.getPackageManagementTimerUnit(), model.getDoFileManagement(), model.getFileManagementTimer(), model.getFileManagementTimerUnit(), model.getTemplate().getName(), null);
	}
}
