package de.cinovo.cloudconductor.server.web.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import de.cinovo.cloudconductor.server.dao.IAgentAuthTokenDAO;
import de.cinovo.cloudconductor.server.dao.IAgentDAO;
import de.cinovo.cloudconductor.server.model.EAgent;
import de.cinovo.cloudconductor.server.model.EAgentAuthToken;
import de.cinovo.cloudconductor.server.util.AuthTokenGenerator;
import de.cinovo.cloudconductor.server.web.CSViewModel;
import de.cinovo.cloudconductor.server.web.helper.AWebPage;
import de.cinovo.cloudconductor.server.web.helper.AjaxAnswer;
import de.cinovo.cloudconductor.server.web.helper.NavbarHardLinks;
import de.cinovo.cloudconductor.server.web.interfaces.IToken;
import de.cinovo.cloudconductor.server.web.interfaces.IWebPath;
import de.taimos.cxf_renderer.model.RenderedUI;
import de.taimos.restutils.RESTAssert;

/**
 * Copyright 2016 Cinovo AG<br>
 * <br>
 * 
 * @author ablehm
 * 
 */
public class TokensImpl extends AWebPage implements IToken {
	
	@Autowired
	private IAgentAuthTokenDAO dToken;
	@Autowired
	private IAgentDAO dAgent;
	@Autowired
	private AuthTokenGenerator tokenGen;
	
	
	@Override
	protected String getTemplateFolder() {
		return "tokens";
	}
	
	@Override
	protected void init() {
		this.navRegistry.registerSubMenu(NavbarHardLinks.config, this.getNavElementName(), IToken.ROOT);
		this.addBreadCrumb(IWebPath.WEBROOT + IToken.ROOT, this.getNavElementName());
		this.addTopAction(IWebPath.WEBROOT + IToken.ROOT + IWebPath.ACTION_ADD, "Create new Token");
	}
	
	@Override
	protected String getNavElementName() {
		return "Auth-Tokens";
	}
	
	@Override
	@Transactional
	public RenderedUI view() {
		List<EAgentAuthToken> tokens = this.dToken.findList();
		CSViewModel view = this.createView();
		view.addModel("TOKENS", tokens);
		// send token-agent map to view
		List<EAgent> agents = this.dAgent.findList();
		Multimap<Long, String> tokenAgentMap = HashMultimap.create();
		for (EAgent agent : agents) {
			tokenAgentMap.put(agent.getToken().getId(), agent.getName());
		}
		view.addModel("TOKENAGENTMAP", tokenAgentMap);
		return view.render();
	}
	
	@Override
	@Transactional
	public RenderedUI newTokenView() {
		CSViewModel modal = this.createModal("mAddToken");
		List<EAgent> agentsWithoutToken = this.dAgent.getAgentsWithoutToken();
		modal.addModel("AGENTSWITHOUTTOKEN", agentsWithoutToken);
		return modal.render();
	}
	
	@Override
	@Transactional
	public RenderedUI editTokenView(String token) {
		RESTAssert.assertNotEmpty(token);
		CSViewModel modal = this.createModal("mModToken");
		modal.addModel("TOKEN", this.dToken.findByToken(token));
		return modal.render();
	}
	
	@Override
	@Transactional
	public AjaxAnswer generateNewToken(String[] agents) {
		try {
			EAgentAuthToken generatedToken = this.tokenGen.generateAuthToken(Integer.parseInt(System.getProperty("cloudconductor.tokenlength", "32")));
			for (String agentId : agents) {
				EAgent agent = this.dAgent.findById(Long.parseLong(agentId));
				agent.setToken(generatedToken);
				this.dAgent.save(agent);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new AjaxAnswer(IWebPath.WEBROOT + IToken.ROOT);
	}
}
