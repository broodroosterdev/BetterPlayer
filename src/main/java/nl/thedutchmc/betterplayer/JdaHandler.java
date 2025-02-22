package nl.thedutchmc.betterplayer;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class JdaHandler {
	
	private JDA jda;
	private final BetterPlayer betterPlayer;
	
	public JdaHandler(BetterPlayer betterPlayer) {
		this.betterPlayer = betterPlayer;
	}
	
	public void initJda(String token) {

		JDABuilder jdaBuilder = JDABuilder.createDefault(token)
				.enableIntents(
						GatewayIntent.GUILD_MESSAGES,
						GatewayIntent.GUILD_VOICE_STATES)
				.setAutoReconnect(true)
				.setActivity(Activity.playing("https://github.com/TheDutchMC/BetterPlayer"));

		try {
			jda = jdaBuilder.build();
			jda.awaitReady();
		} catch(LoginException e) {
			BetterPlayer.logError("Unable to log in to Discord. Is your token valid? Run with --debug for more info.");
			BetterPlayer.logDebug(Utils.getStackTrace(e));
		} catch (InterruptedException e) {
			BetterPlayer.logError("Unable to complete startup. JDA Connection was interrupted. Run with --debug for more info.");
			BetterPlayer.logDebug(Utils.getStackTrace(e));
		}
		
		betterPlayer.getEventManager().registerDefaultEvents(jda);
		
		//betterPlayer.getBetterAudioManager().joinAudioChannel(734870031359541251L);
	}
	
	public void shutdownJda() throws Exception {
		jda.shutdownNow();
	}
	
	public JDA getJda() {
		return this.jda;
	}
}
