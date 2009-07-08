package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.amazon.AmazonAdapter;
import org.nideasystems.webtools.zwitrng.server.amazon.AmazonBook;
import org.nideasystems.webtools.zwitrng.server.amazon.AmazonLocale;
import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
import org.nideasystems.webtools.zwitrng.server.rss.RSS;
import org.nideasystems.webtools.zwitrng.server.rss.RSSItem;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;
import org.nideasystems.webtools.zwitrng.server.twitter.TwitterServiceAdapter;
import org.nideasystems.webtools.zwitrng.server.utils.DataUtils;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterUpdateDTO;

public class RunCampaignsJobServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7705658558425439973L;

	private static final Logger log = Logger
			.getLogger(RunCampaignsJobServlet.class.getName());

	private Date now = new Date();
	// private StringBuffer outBuffer;

	private int hour = 0;
	private static boolean TESTING = false;
	StringBuffer outBuffer = null;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("=============Running Job: Run campaigns=============");
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

		now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		hour = cal.get(Calendar.HOUR_OF_DAY);

		outBuffer.append("now is " + now);
		outBuffer.append("Hour is " + hour);
		try {
			outBuffer.append("<br/>");
			// Get all campaigns with status == RUNNING, RESCHEDULE or
			// NOT_STARTED
			startTransaction(true);
			List<CampaignDO> runningCampaigns = getBusinessHelper()
					.getCampaignPojo().getCampaigns(CampaignStatus.RUNNING);

			if (runningCampaigns != null) {
				outBuffer.append("<h1>Campaigns RUNNING</h1>");
				for (CampaignDO campaign : runningCampaigns) {
					// run
					outBuffer.append(getCampaignDesc(campaign));
					try {
						run(campaign);
					} catch (Exception e) {
						log.severe("Error running the campaign"
								+ e.getMessage());
						log.info("Failing safe");
						e.printStackTrace();
						outBuffer.append("Excpetion in running the campaign: "
								+ e.getMessage());
					}

				}
			}

			runningCampaigns = getBusinessHelper().getCampaignPojo()
					.getCampaigns(CampaignStatus.RESCHEDULED);

			if (runningCampaigns != null) {
				outBuffer.append("<h1>Campaigns RESCHEDULED </h1>");
				for (CampaignDO campaign : runningCampaigns) {
					// run(campaign);
					outBuffer.append(getCampaignDesc(campaign));

				}
			}

			runningCampaigns = getBusinessHelper().getCampaignPojo()
					.getCampaigns(CampaignStatus.NOT_STARTED);

			if (runningCampaigns != null) {
				outBuffer.append("<h1>Campaigns Not Started</h1>");
				for (CampaignDO campaign : runningCampaigns) {
					// run(campaign);
					outBuffer.append(getCampaignDesc(campaign));
				}
			}
			runningCampaigns = getBusinessHelper().getCampaignPojo()
					.getCampaigns(CampaignStatus.FINISHED);

			if (runningCampaigns != null) {
				outBuffer.append("<h1>Campaigns Fnished </h1>");
				for (CampaignDO campaign : runningCampaigns) {
					run(campaign);
					outBuffer.append(getCampaignDesc(campaign));
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
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

	private void run(CampaignDO campaign) throws Exception {

		if (canRun(campaign)) {
			log.info("*******************Sending a Tweet for Campaign: "
					+ campaign.getName());

			PersonaDO persona = campaign.getPersona();
			// Get sending account
			TwitterAccountDO accountDo = persona.getTwitterAccount();

			TwitterAccountDTO accountDto = DataUtils
					.twitterAccountDtoFromDo(accountDo);

			TemplateDO template = getBusinessHelper().getTemplatePojo()
					.getRandomTemplateForCampaign(campaign);

			if (template == null) {
				throw new Exception("Template Not found");
			}

			// Send it
			// Get the text to send in the Tweet
			String updateStatus = template.getText();
			this.outBuffer.append("<div>Using Template Text :" + updateStatus
					+ "<div>");
			// Check if it has a list to load

			// Start get feedSet lists
			List<String> feedSetLists = StringUtils
					.getFeedSetLists(updateStatus);

			for (String feedSetName : feedSetLists) {
				this.outBuffer.append("<div>Found feedSetName :" + feedSetName
						+ "<div>");
				String randomUrl = getBusinessHelper().getFeedSetPojo()
						.getRandomUrl(persona, feedSetName);
				this.outBuffer.append("<div>URL :" + randomUrl + "<div>");
				// now get some content....
				boolean random = feedSetName.contains(":random") ? true : false;
				RSSItem item = RSS.get().getNextRssItem(randomUrl, random);

				if (item != null) {
					outBuffer.append("<div>RSSTitle:" + item.getTitle()
							+ "</div>");
					outBuffer.append("<div>RSSLink:" + item.getLink()
							+ "</div>");
					boolean addLink = true;
					if (feedSetName.contains(":nolink")) {
						addLink = false;
					}
					String rssStatus = RSS.get().createStatusFromRssItem(item,
							addLink);
					updateStatus = StringUtils.replaceRSSItem(updateStatus,
							feedSetName, rssStatus);
					outBuffer.append("<div>STATUS to REPLACE: " + rssStatus
							+ "</div>");
				} else {
					outBuffer.append("<div>NO RSS ITEM FOUND</div>");
					throw new Exception("Could not find a RSS Item to use");
					// Cancel the tweet
				}
			}
			// End get feedSet lists
			List<String> lists = StringUtils.getFragmentLists(updateStatus);
			Map<String, String> mappedValues = null;
			try {
				mappedValues = getBusinessHelper().getTemplatePojo()
						.getFragmentsLists(campaign.getPersona().getName(),
								campaign.getPersona().getUserEmail(), lists);
			} catch (Exception e) {
				log.warning("Error getting template lists" + e.getMessage());
				e.printStackTrace();
				throw new Exception(e);
			}

			if (mappedValues != null) {

				updateStatus = StringUtils.replaceFragmentsLists(updateStatus,
						mappedValues);

			}

			updateStatus = StringUtils.randomizeString(updateStatus);

			TwitterUpdateDTO update = new TwitterUpdateDTO();
			update.setTwitterAccount(accountDto);
			update.setText(updateStatus);

			try {
				// Don't send if in testing mode
				if (!TESTING) {
					TwitterServiceAdapter.get().postUpdate(update);
				}

				outBuffer.append("<div>Tweet Sent: " + update.getText()
						+ "</div>");
			} catch (Exception e) {
				log.warning("Could not send the tweet" + updateStatus
						+ " for campaign " + campaign.getName()
						+ " blonging to user "
						+ campaign.getPersona().getName() + " "
						+ e.getMessage());
				e.printStackTrace();
				outBuffer.append("Could not send the tweet" + updateStatus
						+ " for campaign " + campaign.getName()
						+ " blonging to user "
						+ campaign.getPersona().getName() + " "
						+ e.getMessage());

				throw e;

			}
			//replaceBooks(updateStatus);
			// update used times
			template.setUsedTimes(template.getUsedTimes() + 1);

			long time = new Date().getTime();
			campaign.setLastRun(new Date(time));
			campaign.setTweetsSent(campaign.getTweetsSent() + 1);
			campaign.setNextRun(new Date(time
					+ (getAllowedElapsedMinutes(campaign) * 60 * 1000)));
			log.info("Next Tweet to send at " + campaign.getNextRun());

		}
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
		AmazonLocale locale = affTemplate.contains(":UK") ? AmazonLocale.UK : AmazonLocale.US;
		// Get the search terms
		String searchTerms = affTemplate.substring(affTemplate.indexOf("(")+1,
				affTemplate.indexOf(")"));
		AmazonBook book = AmazonAdapter.get().getRandomBook(searchTerms);
		
		if (book!=null) {
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
		return campaign.getTweetsSent() >= campaign.getMaxTweets();
	}

	private boolean isCampaignRunning(CampaignDO campaign) {
		// TODO Auto-generated method stub
		long offsetMilis = 1000 * 60 * 60 * 23 + 1000 * 60 * 59;
		return now.after(campaign.getStartDate())
				&& now.before(new Date(campaign.getEndDate().getTime()
						+ offsetMilis));

	}

	private boolean isHourToRun(CampaignDO campaign) {
		boolean run = false;

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
		if (campaign.getLastRun() == null) {
			run = true;
		} else {
			long timeElapsed = now.getTime() - campaign.getLastRun().getTime();
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
		if (isCampaignRunning(campaign)) {
			campaign.setStatus(CampaignStatus.RUNNING);
		} else {
			// UpdateStatus
			if (now.after(campaign.getEndDate())) {
				campaign.setStatus(CampaignStatus.FINISHED);
			}
		}
		if (maxTweetsReached(campaign)) {
			campaign.setStatus(CampaignStatus.FINISHED);
		}

	}

	private boolean canRun(CampaignDO campaign) {
		log.info("======Can Running campaign " + campaign.getName()
				+ " for User: " + campaign.getPersona().getName() + "/"
				+ campaign.getPersona().getUserEmail() + "?");

		boolean send = false;
		send = !campaign.getStatus().equals(CampaignStatus.CANCELED)
				&& !campaign.getStatus().equals(CampaignStatus.FINISHED)
				&& !maxTweetsReached(campaign) && isCampaignRunning(campaign)
				&& isHourToRun(campaign)
				&& isTimeSinceLastTweetElapsed(campaign);
		updateCampaignStatus(campaign);

		log.fine("Returning " + send);
		return send;

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
		sb.append("<li><b>Status:" + campaign.getStatus() + "</b></li>");
		sb.append("<li>Persona:" + campaign.getPersona().getName() + " Email:"
				+ campaign.getPersona().getUserEmail() + "</li>");
		sb.append("<li>Tweets Send:" + campaign.getTweetsSent() + "</li>");
		sb.append("<li>Time between Tweets:" + campaign.getTimeBetweenTweets()
				+ "</li>");
		sb.append("<li>Start Date:" + campaign.getStartDate() + "</li>");
		sb.append("<li>End Date:" + campaign.getEndDate() + "</li>");
		sb.append("<li>Start Hour of the Day:"
				+ campaign.getStartHourOfTheDay() + "</li>");
		sb.append("<li>End Hour of the Day:" + campaign.getEndHourOfTheDay()
				+ "</li>");
		sb.append("<li>Last Run At:" + campaign.getLastRun() + "</li>");
		sb.append("<li>Next Run At:" + campaign.getNextRun() + "</li>");
		sb.append("<br/>");
		return sb.toString();
	}

}
