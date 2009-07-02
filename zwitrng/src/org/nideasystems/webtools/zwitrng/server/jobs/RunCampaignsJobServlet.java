package org.nideasystems.webtools.zwitrng.server.jobs;

import java.io.IOException;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nideasystems.webtools.zwitrng.server.domain.CampaignDO;
import org.nideasystems.webtools.zwitrng.server.servlets.AbstractHttpServlet;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignStatus;
import org.nideasystems.webtools.zwitrng.shared.model.TimeUnits;

public class RunCampaignsJobServlet extends AbstractHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7705658558425439973L;

	private static final Logger log = Logger
			.getLogger(RunCampaignsJobServlet.class.getName());

	private Date now = new Date();

	private int hour = 0;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.info("=============Running Job: Run campaigns=============");
		StringBuffer sb = new StringBuffer();

		now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		hour = cal.get(Calendar.HOUR_OF_DAY);

		
		sb.append("now is " + now);
		sb.append("Hour is " + hour);
		try {
			sb.append("<br/>");
			// Get all campaigns with status == RUNNING, RESCHEDULE or
			// NOT_STARTED
			startTransaction(true);
			List<CampaignDO> runningCampaigns = getBusinessHelper()
					.getCampaignPojo().getCandidateCampaignsToRun(
							CampaignStatus.RUNNING);

			if (runningCampaigns != null) {
				sb.append("<h1>Campaigns in RUNNING status: </h1>");
				for (CampaignDO campaign : runningCampaigns) {
					// run
					run(campaign);
					sb.append(getCampaignDesc(campaign));
				}
			}

			runningCampaigns = getBusinessHelper().getCampaignPojo()
					.getCandidateCampaignsToRun(CampaignStatus.RESCHEDULED);

			if (runningCampaigns != null) {
				sb.append("<h1>Campaigns in RESCHEDULED status: </h1>");
				for (CampaignDO campaign : runningCampaigns) {
					run(campaign);
					sb.append(getCampaignDesc(campaign));

				}
			}

			runningCampaigns = getBusinessHelper().getCampaignPojo()
					.getCandidateCampaignsToRun(CampaignStatus.NOT_STARTED);

			if (runningCampaigns != null) {
				sb.append("<h1>Campaigns in Not Started status: </h1>");
				for (CampaignDO campaign : runningCampaigns) {
					run(campaign);
					sb.append(getCampaignDesc(campaign));
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
		resp.getWriter().println(sb.toString());
	}

	private void run(CampaignDO campaign) {
		log.fine("RUN==============");
		
		if ( canRun(campaign)) {
			log.info("Sending a Tweet for Campaign: "+campaign.getName());
			long time = new Date().getTime();
			campaign.setLastRun(new Date(time));
			campaign.setTweetsSent(campaign.getTweetsSent()+1);
			campaign.setNextRun(new Date( time+ (getAllowedElapsedMinutes(campaign)*60*1000) ));
			log.info("Next Tweet to send at "+campaign.getNextRun());
		}
	}

	private boolean canRun(CampaignDO campaign) {
		log.info("======Running campaign " + campaign.getName() + " for User: "
				+ campaign.getPersona().getName() + "/"
				+ campaign.getPersona().getUserEmail());

		boolean send = false;
		// Check if the current date is greater than the campaign start date
		//The end date must be the last hour 23:59
		//Ofset 
		long offsetMilis = 1000*60*60*23+1000*60*59;
		
		if (now.after(campaign.getStartDate())
				&& now.before(new Date(campaign.getEndDate().getTime()+offsetMilis))) {
			// Ok, it's time to run
			// Change the status to RUNNIN
			campaign.setStatus(CampaignStatus.RUNNING);
			// Check if it's a good hour to run
			if ( campaign.getStartHourOfTheDay()!= null && campaign.getEndHourOfTheDay()!= null ) {
				log.fine("===Start hour is: "+campaign.getStartHourOfTheDay());
				log.fine("===End hour is: "+campaign.getEndHourOfTheDay());
				if ( hour>=campaign.getStartHourOfTheDay() && hour < campaign.getEndHourOfTheDay() ) {
					log.fine("Send a TWEET");
					send = true;
				} else {
					log.fine("It's Not the hour to send a TWEEET");
				}
			} else {
				//Hours not set, the ignore
				send = true;
			}
			
			//Check the last time it runned
			if (send) {
				if ( campaign.getLastRun()!=null) {
					long timeElapsed = now.getTime()-campaign.getLastRun().getTime();
					long elapsedMinutes = timeElapsed / (60*1000);
					log.fine("===ELAPSED Minutes:"+elapsedMinutes);
					
					long allowedElapsedMinutes = getAllowedElapsedMinutes(campaign);/*campaign.getTimeBetweenTweets();
					if ( campaign.getTimeUnit().equals(TimeUnits.HOURS)) {
						allowedElapsedMinutes = allowedElapsedMinutes*60;
					} else if (campaign.getTimeUnit().equals(TimeUnits.DAYS)) {
						allowedElapsedMinutes = allowedElapsedMinutes*60*24;
					}*/
					log.fine("ALLOWED ELAPSED Minutes: "+allowedElapsedMinutes);
					
					if ( elapsedMinutes < allowedElapsedMinutes ) {
						log.fine("WILL NOT TWEET");
						send=false;
					} 
				} 
			}
			log.fine("==It's Time to run");
		} else if (now.after(campaign.getEndDate())) {
			log.fine("==The campaign is ended.");
			campaign.setStatus(CampaignStatus.FINISHED);
		} else {
			if ( !campaign.getStatus().equals(CampaignStatus.RESCHEDULED) ) {
				campaign.setStatus(CampaignStatus.NOT_STARTED);
			}
			log.fine("==The campaign does not started yet");
		}
		log.fine("====End running Campaign===");
		return send;

	}

	private long getAllowedElapsedMinutes(CampaignDO campaign) {
		long allowedElapsedMinutes = campaign.getTimeBetweenTweets();
		if ( campaign.getTimeUnit().equals(TimeUnits.HOURS)) {
			allowedElapsedMinutes = allowedElapsedMinutes*60;
		} else if (campaign.getTimeUnit().equals(TimeUnits.DAYS)) {
			allowedElapsedMinutes = allowedElapsedMinutes*60*24;
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
