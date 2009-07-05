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

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.domain.PersonaDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateDO;
import org.nideasystems.webtools.zwitrng.server.domain.TemplateFragmentDO;
import org.nideasystems.webtools.zwitrng.server.domain.TwitterAccountDO;
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
					run(campaign);

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

	private void run(CampaignDO campaign) {

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

			if (template != null) {
				// Send it
				// Get the text to send in the Tweet
				String updateStatus = template.getText();

				// Check if it has a list to load
				List<String> lists = StringUtils.getFragmentLists(updateStatus);

				/*for (String listName: lists) {
					//If it have a list, check if the campaign has run before with this template
					String fragText = campaign.getTempData().get("__frag:::"+listName+":::"+template.getKey());
					//Haven't loaded it yest
					if (fragText == null) {
						//Load it
						try {
							TemplateFragmentDO fragment = getBusinessHelper()
									.getTemplateDao().findTemplateFragmentByName(
											persona, listName);
							if ( fragment != null ) {
								campaign.addTempData("__frag:::"+listName+":::"+template.getKey(), fragment.getText());
								campaign.addTempData("__nextFragIndex:::"+listName+":::"+template.getKey(),"0");
							}
						} catch (Exception e) {
							log.severe("Error getting the Template Fragment: "
									+ e.getMessage());
							e.printStackTrace();
						}

						//check if order is to maintain
					}
				}
				
				//Now we have the lists loaded for template for list
				
				// now do your magic

				// only if has order
				// check if tempData of campaign has template key in

				// Get the Lists
				List<TemplateFragmentDO> fragments = new ArrayList<TemplateFragmentDO>();
				
				//Do I have any fragment loaded here?
				for (TemplateFragmentDO tFrag: fragments ) {
					campaign.addTempData("__frag:::"+tFrag.getName()+":::"+template.getKey(),tFrag.getText());
					
				}*/
				// campaign.getRandomListForTemplate(list+"_"template.getKey());
				// campaign.getNextIndexForListTemplate(list+"_"+template.getKey());

				Map<String, String> mappedValues = null;
				try {
					mappedValues = getBusinessHelper()
							.getTemplatePojo()
							.getFragmentsLists(campaign.getPersona().getName(),
									campaign.getPersona().getUserEmail(), lists);
				} catch (Exception e) {
					log
							.warning("Error getting template lists"
									+ e.getMessage());
					e.printStackTrace();
					// Continue
				}

				if (mappedValues != null) {

					updateStatus = StringUtils.replaceFragmentsLists(
							updateStatus, mappedValues);

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

				}

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