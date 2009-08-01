package org.nideasystems.webtools.zwitrng.server.jobs.campaigns;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.Configuration;
import org.nideasystems.webtools.zwitrng.server.amazon.AmazonAdapter;
import org.nideasystems.webtools.zwitrng.server.amazon.AmazonBook;
import org.nideasystems.webtools.zwitrng.server.amazon.AmazonLocale;
import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.dao.TwitterAccountDAO;
import org.nideasystems.webtools.zwitrng.server.jobs.Job;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

import com.google.appengine.api.datastore.Key;

public class CreateCampaignsJobServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7705658558425439973L;

	private static final Logger log = Logger
			.getLogger(CreateCampaignsJobServlet.class.getName());

	private Date startTime = new Date();
	// private StringBuffer outBuffer;

	// Hour of the day
	private int hour = 0;
	private static boolean TESTING = false;
	StringBuffer outBuffer = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("=============R U N     C A M P  A I G N S=============");
		// Check headers
		outBuffer = new StringBuffer();

		if (!TESTING) {
			if (req.getHeader("X-AppEngine-Cron") == null
					|| !req.getHeader("X-AppEngine-Cron").equals("true")) {
				log.severe("Job called outside of a cron context");
				throw new ServletException(
						"Job called outside of a cron context");

			}

		}

		startTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startTime);
		hour = cal.get(Calendar.HOUR_OF_DAY);
		getBusinessHelper().setStartTime(startTime.getTime());
		outBuffer.append("now is :" + startTime);
		outBuffer.append("Hour is :" + hour);
		// Will add the jobs to a queue and then run them serialized per person
		try {
			List<Key> personasKey = new ArrayList<Key>();
			startTransaction(true);
			List<PersonaDO> allActivePersonas = getBusinessHelper()
					.getPersonaDao().findAllActivePersonas();
			for (PersonaDO persona: allActivePersonas) {
				personasKey.add(persona.getKey());
			}
			endTransaction();

			/*
			 * // Get the last persona checked int lastPersonaUsed =
			 * getLastPersonaUsed();
			 * 
			 * if (lastPersonaUsed >= allActivePersonas.size()) {
			 * lastPersonaUsed = 0; }
			 */
			// PersonaDO persona =
			// null;//allActivePersonas.get(lastPersonaUsed);
			for (Key personaKey : personasKey) {
				
				startTransaction(true);
				PersonaDO persona = getBusinessHelper().getPersonaDao().findPersona(personaKey);
				processPersona(persona);
				
				endTransaction();

				long now = new Date().getTime();
				if (now-startTime.getTime()>=Configuration.MAX_JOB_RUN_TIME) {
					log.fine("############Time elapsed. Ending with last processed: "+persona.getName());
					break;
				}	
			}
			

			// }
		} catch (Exception e) {
			log.severe("Error Enqueueing the campaign"+e.getMessage());
			e.printStackTrace();
			throw new ServletException(e);
		} finally {
			endTransaction();
		}

		resp.setContentType("text/html");
		if (TESTING) {
			resp.getWriter().println(outBuffer.toString());
		} else {
			resp.getWriter().println("200 OK");
		}

	}

	private void processPersona(PersonaDO persona) throws Exception{

		log.fine("######### Starting enqueueing TWEETS for Persona: "
				+ persona.getName());

		// lastPersonaUsed++;
		// setNextPersonaUsed(lastPersonaUsed);
		// for (PersonaDO persona : allActivePersonas) {

		outBuffer
				.append("<h2>Going to check the personas that have campaigns to run: "
						+ persona.getName()
						+ "[Email: "
						+ persona.getUserEmail() + "]</h2>");

		// Get all campaigns for this persona that are started

		if (TESTING) {

			List<CampaignDO> campaigns = getBusinessHelper().getCampaignDao()
					.findCampaignsByPersonaAndStatus(persona,
							CampaignStatus.NOT_STARTED);
			outBuffer.append("<h3>Not Started Campaigns</h3>");
			for (CampaignDO campaign : campaigns) {

				outBuffer.append(getCampaignDesc(campaign));

			}

			campaigns = getBusinessHelper().getCampaignDao()
					.findCampaignsByPersonaAndStatus(persona,
							CampaignStatus.STARTED);
			outBuffer.append("<h3>Started Campaigns</h3>");
			for (CampaignDO campaign : campaigns) {
				outBuffer.append(getCampaignDesc(campaign));
				enqueue(campaign);
			}

			campaigns = getBusinessHelper().getCampaignDao()
					.findCampaignsByPersonaAndStatus(persona,
							CampaignStatus.PAUSED);
			outBuffer.append("<h3>Paused Campaigns</h3>");
			for (CampaignDO campaign : campaigns) {
				outBuffer.append(getCampaignDesc(campaign));
			}

			campaigns = getBusinessHelper().getCampaignDao()
					.findCampaignsByPersonaAndStatus(persona,
							CampaignStatus.STOPPED);
			outBuffer.append("<h3>Stoped Campaigns</h3>");
			for (CampaignDO campaign : campaigns) {
				outBuffer.append(getCampaignDesc(campaign));
			}

			campaigns = getBusinessHelper().getCampaignDao()
					.findCampaignsByPersonaAndStatus(persona,
							CampaignStatus.FINISHED);
			outBuffer.append("<h3>Finished Campaigns</h3>");
			for (CampaignDO campaign : campaigns) {
				outBuffer.append(getCampaignDesc(campaign));
			}

		} else {
			List<CampaignDO> campaigns = getBusinessHelper().getCampaignDao()
					.findCampaignsByPersonaAndStatus(persona,
							CampaignStatus.STARTED);

			for (CampaignDO campaign : campaigns) {

				try {

					enqueue(campaign);
				} catch (Exception e) {
					log.warning("Error Enqueueing the campaign "
							+ campaign.getName() + " for User: "
							+ persona.getName() + "/Email:"
							+ persona.getUserEmail());
					// Continue
				}

			}

		}

	}

	private void setNextPersonaUsed(int lastPersonaUsed) throws Exception {
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(
					Collections.emptyMap());
		} catch (CacheException e) {
			throw new Exception(e);
		}
		cache.put("nextPersonaInCampaigns", lastPersonaUsed);
	}

	private int getLastPersonaUsed() throws Exception {
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(
					Collections.emptyMap());
		} catch (CacheException e) {
			throw new Exception(e);
		}
		// Try to get the list from cahc
		Integer nextPersonaCampaign = (Integer) cache
				.get("nextPersonaInCampaigns");

		if (nextPersonaCampaign == null) {
			nextPersonaCampaign = new Integer(0);
			cache.put("lastPersonaInCampaigns", nextPersonaCampaign);
		}
		return nextPersonaCampaign;

	}

	private String selectTemplate(CampaignDO campaign) throws Exception {
		if (campaign.getRunningInstance().getTweetTemplates() == null) {
			// getBusinessHelper().getCampaignPojo().buildTweetTemplates(campaign);

			// if (campaign.getRunningInstance().getTweetTemplates().size() ==
			// 0) {
			throw new Exception("No templates found. Null");
			// }
		}

		if (TESTING) {
			dumpTweetTemplates(campaign);
		}

		String tweetTemplate = null;
		// Now check if is to run randomly
		if (campaign.getUseTemplatesRandomly()) {
			// get some index
			int index = getRandomNumber(campaign.getRunningInstance()
					.getTweetTemplates().size());
			// get a tweet template
			tweetTemplate = campaign.getRunningInstance().getTweetTemplates()
					.get(index);
		} else {

			int index = campaign.getRunningInstance()
					.getNextTemplateNameIndex();

			if (index < campaign.getRunningInstance().getTweetTemplates()
					.size()) {
				tweetTemplate = campaign.getRunningInstance()
						.getTweetTemplates().get(index);

			} else if (campaign.getAllowRepeatTemplates()) {
				index = 0;
				campaign.getRunningInstance().setNextTemplateNameIndex(index);
				tweetTemplate = campaign.getRunningInstance()
						.getTweetTemplates().get(index);
			}
			if (campaign.getAllowRepeatTemplates()) {
				campaign.getRunningInstance().setNextTemplateNameIndex(
						index + 1);
			}

		}

		if (tweetTemplate != null) {

			if (!campaign.getAllowRepeatTemplates()) {
				campaign.getRunningInstance().getTweetTemplates().remove(
						tweetTemplate);
			}

			// goto

		}
		if (campaign.getRunningInstance().getTweetTemplates() == null
				|| campaign.getRunningInstance().getTweetTemplates().size() == 0) {
			if (campaign.getAllowRepeatTemplates()) {
				getBusinessHelper().getCampaignPojo().buildTweetTemplates(
						campaign);
				campaign.getRunningInstance().setNextTemplateNameIndex(-1);
			} else {
				campaign.setStatus(CampaignStatus.FINISHED);
			}
		}
		return tweetTemplate;
	}

	private void enqueue(CampaignDO campaign) throws Exception {

		if (TESTING) {
			outBuffer.append("<div>Checking to enque the campaign "
					+ campaign.getName() + "</div>");
		}

		// Check if the campaing can run now
		if (canRun(campaign)) {
			if (TESTING) {
				outBuffer.append("<div>Enqueueing campaign "
						+ campaign.getName() + "</div>");

			}

			String tweetTemplate = selectTemplate(campaign);

			if (TESTING) {
				outBuffer.append("<h3>TweetTemplate Selected: " + tweetTemplate
						+ "</h3>");
			}

			String finalStatus = getBusinessHelper().getTemplatePojo()
					.replaceTemplateLists(campaign.getPersona(), tweetTemplate);
			// Replace random if any
			finalStatus = StringUtils.randomizeTemplate(finalStatus);

			// Now deal with the fucking feeds

			finalStatus = getBusinessHelper().getFeedSetPojo().replaceFeeds(
					campaign, finalStatus);
			if (finalStatus != null) {
				// Skip this
				if (TESTING) {
					outBuffer
							.append("<h3>Sent Tweet: " + finalStatus + "</h3>");
				}

				try {
					TwitterAccountDTO accountDto = TwitterAccountDAO
							.createAuthorizedAccountDto(campaign.getPersona()
									.getTwitterAccount());
					TwitterUpdateDTO update = new TwitterUpdateDTO();
					update.setSendingTwitterAccount(accountDto);
					update.setText(finalStatus);

					Job job = new Job();
					job.setJobClassName(SendTweetJob.class.getName());
					job.setPersonaKey(campaign.getPersona().getKey());
					job.addParameter("status", update);
					// Create a job
					enqueueJob(job);
					// getBusinessHelper().getTwitterPojo().postUpdate(update);

				} catch (Exception e) {
					log.severe("Error enqueuing the job for campaign:"
							+ e.getMessage());
					e.printStackTrace();
					throw e;
				}

				//
				// Final Steps
				campaign.getRunningInstance().setTweetsSent(
						campaign.getRunningInstance().getTweetsSent() + 1);

				campaign.getRunningInstance().setLastRun(new Date());
				campaign
						.getRunningInstance()
						.setNextRun(
								new Date(
										campaign.getRunningInstance()
												.getLastRun().getTime()
												+ (getAllowedElapsedMinutes(campaign) * 60 * 1000)));
				campaign.getRunningInstance().setInfo(
						"Last Tweet sent:" + finalStatus + " at "
								+ campaign.getRunningInstance().getLastRun());
				if (maxTweetsReached(campaign)) {
					campaign.setStatus(CampaignStatus.FINISHED);
				}

			}

		} else {
			if (TESTING) {
				outBuffer.append("<div>Cannot enqueue the  campaign "
						+ campaign.getName() + " now</div>");
			}

		}
	}

	private void enqueueJob(Job job) throws Exception {
		// Get the cache
		log.fine("############ ENQUEUING TWEET ##############");
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(
					Collections.emptyMap());
		} catch (CacheException e) {
			throw new Exception(e);
		}
		// Try to get the list from cahc
		List<Job> jobList = (List<Job>) cache.get("tweetjobs");

		if (jobList == null) {
			jobList = new ArrayList<Job>();
		}
		jobList.add(job);
		cache.put("tweetjobs", jobList);

		log.fine("############ Tweet Cache is: " + jobList.size());
	}

	private void dumpTweetTemplates(CampaignDO campaign) {
		outBuffer.append("<div>Dumping tweet Templates</div>");
		for (String temp : campaign.getRunningInstance().getTweetTemplates()) {
			outBuffer.append("<div>" + temp + "</div>");
		}
	}

	private int getRandomNumber(int size) {
		int result = 0;

		if (size > 0) {
			double rand = Math.random();
			result = (int) Math.round(rand * (size - 1));
		}
		return result;

	}

	private List<String> getAffiliateLinks(String updateStatus) {
		List<String> retList = new ArrayList<String>();
		int startIndex = updateStatus.indexOf("%%");

		if (startIndex >= 0) {
			int endIndex = updateStatus.indexOf("%%", startIndex + 2);
			if (endIndex > 0) {
				retList.add(updateStatus.substring(startIndex + 2, endIndex));
				retList.addAll(getAffiliateLinks(updateStatus.substring(
						endIndex + 1, updateStatus.length())));
			}

		}

		return retList;
	}

	private CharSequence getBookStatus(String affTemplate) {
		String retVal = "";
		boolean includeTitle = affTemplate.contains(":title") ? true : false;
		boolean includePrice = affTemplate.contains(":price") ? true : false;
		boolean includeAuthor = affTemplate.contains(":author") ? true : false;
		boolean includeLink = affTemplate.contains(":link") ? true : false;
		AmazonLocale locale = affTemplate.contains(":UK") ? AmazonLocale.UK
				: AmazonLocale.US;
		// Get the search terms
		String searchTerms = affTemplate.substring(
				affTemplate.indexOf("(") + 1, affTemplate.indexOf(")"));
		AmazonBook book = AmazonAdapter.get().getRandomBook(searchTerms);

		if (book != null) {
			affTemplate = affTemplate.replace(":title", book.getTitle());
			affTemplate = affTemplate.replace(":price", book.getLowestPrice());
			affTemplate = affTemplate.replace(":author", book.getAuthor());
			affTemplate = affTemplate.replace(":link", book.getDetailUrl());

		} else {
			affTemplate = "invalid";
		}

		return affTemplate;
	}

	private String replaceBooks(String updateStatus) {
		// format to find: this is %%:book :title :price :author :link
		// :filter(term1 term2)%%
		// replace by:title price author link

		// Find any string inside of %%;

		for (String affTemplate : getAffiliateLinks(updateStatus)) {
			if (affTemplate.contains(":book")) {
				updateStatus = updateStatus.replace(affTemplate,
						getBookStatus(affTemplate));
			}
			outBuffer.append("<div>--" + updateStatus + "</div>");
		}

		return updateStatus;
	}

	private boolean maxTweetsReached(CampaignDO campaign) {

		return campaign.getRunningInstance().getTweetsSent() >= campaign
				.getMaxTweets()
				&& campaign.getLimitNumberOfTweetsSent();
	}

	private boolean isCampaignInRunTimeInterval(CampaignDO campaign) {
		long offsetMilis = 1000 * 60 * 60 * 23 + 1000 * 60 * 59;
		return startTime.after(campaign.getStartDate())
				&& startTime.before(new Date(campaign.getEndDate().getTime()
						+ offsetMilis));

	}

	private boolean isHourToRun(CampaignDO campaign) {
		boolean run = false;
		log.fine("#########################HOUR is " + hour
				+ "  ######################");
		if (campaign.getStartHourOfTheDay() != null
				&& campaign.getEndHourOfTheDay() != null) {

			if (hour >= campaign.getStartHourOfTheDay()
					&& hour <= campaign.getEndHourOfTheDay()) {
				log.fine("Send a TWEET");
				run = true;
			}
		} else {
			run = true;// Run all day
		}
		return run;
	}

	private boolean isTimeSinceLastTweetElapsed(CampaignDO campaign) {

		boolean run = false;
		if (campaign.getRunningInstance().getLastRun() == null) {
			run = true;
		} else {
			long timeElapsed = startTime.getTime()
					- campaign.getRunningInstance().getLastRun().getTime();
			long elapsedMinutes = timeElapsed / (60 * 1000);

			long allowedElapsedMinutes = getAllowedElapsedMinutes(campaign);
			if (TESTING) {
				allowedElapsedMinutes = 0;
			}

			if (elapsedMinutes >= allowedElapsedMinutes) {

				run = true;
			}

		}
		return run;
	}

	public void updateCampaignStatus(CampaignDO campaign) {
		if (isCampaignInRunTimeInterval(campaign)) {
			campaign.setStatus(CampaignStatus.STARTED);
		} else {
			// UpdateStatus
			if (startTime.after(campaign.getEndDate())) {
				campaign.setStatus(CampaignStatus.FINISHED);
			}
		}
		if (maxTweetsReached(campaign)) {
			campaign.setStatus(CampaignStatus.FINISHED);
		}

	}

	private boolean canRun(CampaignDO campaign) {
		log.info("Checking if we can Run the campaign " + campaign.getName()
				+ " for User: " + campaign.getPersona().getName() + "/"
				+ campaign.getPersona().getUserEmail());

		boolean canSend = false;
		canSend = campaign.getStatus().equals(CampaignStatus.STARTED)
				&& !maxTweetsReached(campaign)
				&& isCampaignInRunTimeInterval(campaign)
				&& isHourToRun(campaign)
				&& isTimeSinceLastTweetElapsed(campaign);

		log.fine("Returning " + canSend);
		return canSend;

	}

	private long getAllowedElapsedMinutes(CampaignDO campaign) {
		long allowedElapsedMinutes = campaign.getTimeBetweenTweets();
		if (campaign.getTimeUnit().equals(TimeUnits.HOURS)) {
			allowedElapsedMinutes = allowedElapsedMinutes * 60;
		} else if (campaign.getTimeUnit().equals(TimeUnits.DAYS)) {
			allowedElapsedMinutes = allowedElapsedMinutes * 60 * 24;
		}
		return allowedElapsedMinutes;
	}

	private String getCampaignDesc(CampaignDO campaign) {
		StringBuffer sb = new StringBuffer();
		sb.append("<hr>");

		sb.append("<li>Name:" + campaign.getName() + "</li>");
		sb.append("<li>Use Templates: ");

		for (String cam : campaign.getTemplateNames()) {
			sb.append(cam + " ");
		}
		sb.append("</li>");
		sb.append("<li>Use templates Randomly:"
				+ campaign.getUseTemplatesRandomly() + "</li>");
		sb.append("<li>Repeat Templates:" + campaign.getAllowRepeatTemplates()
				+ "</li>");
		sb.append("<li>Track Links :" + campaign.getTrackClicksOnLinks()
				+ "</li>");

		sb.append("<li><b>Status:" + campaign.getStatus() + "</b></li>");
		sb.append("<li>Persona:" + campaign.getPersona().getName() + " Email:"
				+ campaign.getPersona().getUserEmail() + "</li>");

		sb.append("<li>Limit Tweets:" + campaign.getLimitNumberOfTweetsSent()
				+ "</li>");
		sb.append("<li>Send Max Tweets:" + campaign.getMaxTweets() + "</li>");
		sb.append("<li>Tweets Send:"
				+ campaign.getRunningInstance().getTweetsSent() + "</li>");

		sb.append("<li>Time between Tweets:" + campaign.getTimeBetweenTweets()
				+ "</li>");
		sb.append("<li>Start Date:" + campaign.getStartDate() + "</li>");
		sb.append("<li>End Date:" + campaign.getEndDate() + "</li>");
		sb.append("<li>Start Hour of the Day:"
				+ campaign.getStartHourOfTheDay() + "</li>");
		sb.append("<li>End Hour of the Day:" + campaign.getEndHourOfTheDay()
				+ "</li>");
		sb.append("<li>Last Run At:"
				+ campaign.getRunningInstance().getLastRun() + "</li>");
		sb.append("<li>Next Run At:"
				+ campaign.getRunningInstance().getNextRun() + "</li>");
		sb.append("<br/>");
		return sb.toString();
	}

}
