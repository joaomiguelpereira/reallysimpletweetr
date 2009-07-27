package org.nideasystems.webtools.zwitrng.client.view.users;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.shared.AutoFollowTriggerType;
import org.nideasystems.webtools.zwitrng.shared.model.AutoFollowRuleDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class AbstractRuleConfiguration extends VerticalPanel implements AutoFollowRuleCallback {

	protected Image waitingImg = new Image(Constants.WAITING_IMAGE);
	protected AutoFollowRuleDTO rule;
	private AutoFollowTriggerType type;
	protected CheckBox enabled;
	protected Button save = new Button("Save");
	public AbstractRuleConfiguration(AutoFollowTriggerType type, String title) {
		super();
		waitingImg.setVisible(false);
		this.add(waitingImg);
		this.type = type;
		this.add(new InlineHTML("<h2>"+title+"</h2>"));
		enabled = new CheckBox("Enabled");
		this.add(enabled);
		this.save.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				saveAutoFollowRule();
				
			}
			
		});
	}
	
	
	@Override
	public void onError(Throwable tr) {
		waitingImg.setVisible(false);
		MainController.getInstance().addException(tr);

	}

	public void loadRule() {
		if (this.rule == null) {
			waitingImg.setVisible(true);
			MainController.getInstance().getCurrentPersonaController()
					.loadRule(type, this);

		}
	}

	@Override
	public void onAutoFollowRuleLoaded(AutoFollowRuleDTO rule) {
		this.rule= rule;
		refresh();
		refreshSummary();
	}

	@Override
	public void onAutoFollowRuleChanged(AutoFollowRuleDTO rule) {
		this.rule= rule;
		refresh();
		refreshSummary();
		
	}
	

	public abstract void refreshSummary();

	public abstract void refresh();
	public abstract void saveAutoFollowRule();

}

