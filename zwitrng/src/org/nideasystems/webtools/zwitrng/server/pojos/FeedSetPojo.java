package org.nideasystems.webtools.zwitrng.server.pojos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.CampaignInstanceDO;
import org.nideasystems.webtools.zwitrng.server.domain.FeedSetDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.RSSItemDO;
import org.nideasystems.webtools.zwitrng.server.rss.RSS;
import org.nideasystems.webtools.zwitrng.server.rss.RSSItem;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;

import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTO;
import org.nideasystems.webtools.zwitrng.shared.model.FeedSetDTOList;

public class FeedSetPojo extends AbstractPojo {

	private final int MIN_REFRESH_RSS_INTERVAL = 3600; // seconds

	private final static Logger log = Logger.getLogger(FeedSetPojo.class
			.getName());

	public FeedSetDTOList findFeedSets(String name, String email)
			throws Exception {

		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);

		log.fine("Finding FeedSets");
		if (persona == null) {
			throw new Exception("Persona not found");
		}
		FeedSetDTOList retList = new FeedSetDTOList();

		if (persona.getFeedSets() != null) {
			for (FeedSetDO dom : persona.getFeedSets()) {
				log.fine("Found feed: " + dom.getName());
				retList.addFeedSet(DataUtils.feedSetDtoFromDo(dom));
				log.fine("adding Feed Set:");
			}
		}

		return retList;

	}

	public FeedSetDTO createFeedSet(String name, String email, FeedSetDTO object)
			throws Exception {

		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		FeedSetDO feedSetDom = businessHelper.getFeedSetDao().findByName(
				persona, object.getName());

		if (feedSetDom != null) {
			throw new Exception("A FeedSet with the same name already exixts");
		}

		return businessHelper.getFeedSetDao().create(persona, object);

	}

	public FeedSetDTO saveFeedSet(String name, String email, FeedSetDTO object)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		FeedSetDO feedSetDom = businessHelper.getFeedSetDao().findByName(
				persona, object.getName());

		if (feedSetDom == null) {
			throw new Exception("The feed does not exists.");
		}

		return businessHelper.getFeedSetDao().save(feedSetDom, object);
	}

	public void deleteFeedSet(String name, String email, FeedSetDTO object)
			throws Exception {
		PersonaDO persona = businessHelper.getPersonaDao()
				.findPersonaByNameAndEmail(name, email);
		if (persona == null) {
			throw new Exception();
		}

		// Check if the campaign name alreadey exists
		FeedSetDO feedSetDom = businessHelper.getFeedSetDao().findByName(
				persona, object.getName());

		if (feedSetDom == null) {
			throw new Exception("The feed does not exists.");
		}

		businessHelper.getFeedSetDao().remove(persona, feedSetDom);

	}

	public String getRandomUrl(PersonaDO persona, String feedSetName) {
		String retVal = "";
		// Get the persona feed name
		FeedSetDO feedSetList = businessHelper.getFeedSetDao().findByName(
				persona, feedSetName);

		if (feedSetList != null && feedSetList.getFeedUrls() != null) {

			double rand = Math.random();
			int index = (int) Math.round(rand
					* (feedSetList.getFeedUrls().size() - 1));
			retVal = feedSetList.getFeedUrls().get(index);
		}
		return retVal;

	}

	public String replaceFeeds(CampaignDO campaign, String tweetTemplate)
			throws Exception {

		log.fine("Replacing Feeds By Content");
		// format ((feedname))
		String result = tweetTemplate;
		List<String> feedSetLists = StringUtils.getFeedNames(tweetTemplate);

		// I now have thse name of the feeds
		//
		// try to find the feed by name
		for (String feedName : feedSetLists) {
			log.fine("Trying to porcess FEDD: " + feedName);
			// find the feed
			FeedSetDO feedDom = businessHelper.getFeedSetDao().findByName(
					campaign.getPersona(), feedName);

			if (feedDom != null) {
				log.fine("Processing FEED " + feedDom.getName());
				// now what
				// Get a random URL
				String url = getRandomUrl(feedDom);
				// Try to find the RssItems loaded to use in this campaign
				log.fine("Processing URL " + url);

				// now get the most recent title for the url
				List<RSSItemDO> rssItems = businessHelper.getCampaignDao()
						.findRssItemsToUse(campaign, url);
				// if null or emptry, the populate
				if (rssItems == null || rssItems.size() == 0) {
					// refresh the items
					log.fine("RSS ITEMS IS NULL, REFRESHING:...");
					rssItems = refreshRssItems(campaign, url);
				}

				String newText = "NO CONTENT";
				if (rssItems != null && rssItems.size() > 0) {
					// Get one RSS ITEM;
					RSSItemDO itemDo = campaign.getRunningInstance()
							.getRssItems().get(0);

					if (itemDo != null) {
						newText = "[" + itemDo.getTitle() + " content]";
						campaign.getRunningInstance().getRssItems().remove(0);
						campaign.getRunningInstance().addUsedFeedTitle(
								itemDo.getTitle());
					}

				}
				result = result.replace("((" + feedName + "))", newText);

			} else {
				throw new Exception("Feed not found: " + feedName);
			}
		}

		return result;
	}

	private List<RSSItemDO> refreshRssItems(CampaignDO campaignDO, String url)
			throws Exception {

		// Now fetech the Item from the URL

		List<RSSItem> rssItems = null;
		// check if can run
		Long lastTime = campaignDO.getRunningInstance().getLastTimeRSSFetched();
		CampaignInstanceDO cInstance = campaignDO.getRunningInstance();
		boolean runNow = false;
		if (lastTime == null) {
			lastTime = new Date().getTime();
			campaignDO.getRunningInstance().setLastTimeRSSFetched(lastTime);
			runNow = true;

		}

		Long now = new Date().getTime();
		 

		if (((now - lastTime) > MIN_REFRESH_RSS_INTERVAL*1000) || runNow) {
			rssItems = RSS.get().read(url);
			log
					.fine("-------------------------------------READING RSSS------------------------");
			campaignDO.getRunningInstance().setLastTimeRSSFetched(now);
			// run
		}

		if (rssItems != null) {

			// For each one, check if the title was used before
			boolean use = true;

			for (RSSItem item : rssItems) {
				if (cInstance.getUsedFeedTitles() != null) {
					if (cInstance.getUsedFeedTitles().contains(item.getTitle())) {
						use = false;
					} else {
						use = true;
					}
				} else {
					use = true;
				}
				if (use) {
					log.fine("Adding: " + item.getTitle());
					RSSItemDO itemDo = new RSSItemDO();
					itemDo.setTitle(item.getTitle());
					itemDo.setCreated(new Date());
					itemDo.setFeedUrl(url);
					itemDo.setLink(item.getLink());
					cInstance.addRssItem(itemDo);

					cInstance.addUsedFeedTitle(item.getTitle());
				}
			}

		}

		return cInstance.getRssItems();
	}

	private String getRandomUrl(FeedSetDO feedDom) {

		int index = 0;
		if (feedDom.getFeedUrls().size() > 0) {
			double rand = Math.random();
			index = (int) Math.round(rand * (feedDom.getFeedUrls().size() - 1));
		}
		return feedDom.getFeedUrls().get(index);
		// return null;
	}

}
