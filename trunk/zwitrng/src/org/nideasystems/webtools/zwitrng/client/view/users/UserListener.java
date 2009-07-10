package org.nideasystems.webtools.zwitrng.client.view.users;

public interface UserListener {

	public void onFollowSucess();
	public void onError(Throwable tr);
	public void onUnFollowSucess();
	public void onBlockSucess();
	public void onUnblockSucess();
}
