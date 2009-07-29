package org.nideasystems.webtools.zwitrng.server.domain;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;



import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersonaDO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1562556253020519454L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String userEmail;

	@Persistent
	private String name;

	@Persistent
	private Date created;

	@Persistent
	private Date modified;
	@Persistent
	private PersonaStatus status;

	@Persistent
	private TwitterAccountDO twitterAccount;

	@Persistent(mappedBy = "persona", defaultFetchGroup = "true")
	private List<FilterDO> filters;

	@Persistent(mappedBy = "persona", defaultFetchGroup = "true")
	@Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "created desc"))
	private List<CampaignDO> campaigns;

	@Persistent(mappedBy = "persona", defaultFetchGroup = "true")
	@Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "created desc"))
	private List<TemplateDO> templates;

	@Persistent(mappedBy = "persona", defaultFetchGroup = "true")
	@Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "created desc"))
	private List<TemplateFragmentDO> templateFragments;

	@Persistent(mappedBy = "persona", defaultFetchGroup = "true")
	@Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "created desc"))
	private List<FeedSetDO> feedSets;

	@Persistent(mappedBy = "persona", defaultFetchGroup = "true")
	@Order(extensions = @Extension(vendorName = "datanucleus", key = "list-ordering", value = "created desc"))
	private List<AutoFollowRuleDO> autoFollowRules;
	
	@Persistent
	private List<PersonaJobDefDO> jobDefs;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setTwitterAccount(TwitterAccountDO twitterAccount) {
		this.twitterAccount = twitterAccount;
	}

	public TwitterAccountDO getTwitterAccount() {
		return twitterAccount;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setFilters(List<FilterDO> filters) {
		this.filters = filters;
	}

	public List<FilterDO> getFilters() {
		return filters;
	}

	public void addFilter(FilterDO filter) {
		if (this.filters == null)
			this.filters = new ArrayList<FilterDO>();
		this.filters.add(filter);
	}

	public void addtemplate(TemplateDO template) {
		if (this.templates == null) {
			this.templates = new ArrayList<TemplateDO>();
		}
		this.templates.add(template);
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	public void setTemplates(List<TemplateDO> templates) {
		this.templates = templates;
	}

	public List<TemplateDO> getTemplates() {
		return templates;
	}

	public void setTemplateFragments(List<TemplateFragmentDO> templateFragments) {
		this.templateFragments = templateFragments;
	}

	public List<TemplateFragmentDO> getTemplateFragments() {
		return templateFragments;
	}

	public void addCampaign(CampaignDO campaign) {
		if (this.campaigns == null) {
			this.campaigns = new ArrayList<CampaignDO>();
		}
		this.campaigns.add(campaign);
	}

	public void addtemplateFragment(TemplateFragmentDO fragDo) {
		if (this.templateFragments == null) {
			this.templateFragments = new ArrayList<TemplateFragmentDO>();
		}
		this.templateFragments.add(fragDo);

	}

	public void addFeedSet(FeedSetDO feed) {
		if (this.feedSets == null) {
			this.feedSets = new ArrayList<FeedSetDO>();
		}
		this.feedSets.add(feed);
	}

	public void setCampaigns(List<CampaignDO> campaigns) {
		this.campaigns = campaigns;
	}

	public List<CampaignDO> getCampaigns() {
		return campaigns;
	}

	public void setFeedSets(List<FeedSetDO> feedSets) {
		this.feedSets = feedSets;
	}

	public List<FeedSetDO> getFeedSets() {
		return feedSets;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getModified() {
		return modified;
	}

	public void setAutoFollowRules(List<AutoFollowRuleDO> autoFollowRules) {
		this.autoFollowRules = autoFollowRules;
	}

	public List<AutoFollowRuleDO> getAutoFollowRules() {
		return autoFollowRules;
	}

	public void addAutoFollowRule(AutoFollowRuleDO rule) {
		if (this.autoFollowRules == null) {
			this.autoFollowRules = new ArrayList<AutoFollowRuleDO>();
		}

		this.autoFollowRules.add(rule);
	}

	public void setStatus(PersonaStatus status) {
		this.status = status;
	}

	public PersonaStatus getStatus() {
		return status;
	}

	public void setJobDefs(List<PersonaJobDefDO> jobDefs) {
		this.jobDefs = jobDefs;
	}

	public List<PersonaJobDefDO> getJobDefs() {
		return jobDefs;
	}

	
	

	

}
