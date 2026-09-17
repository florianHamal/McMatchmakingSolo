package at.flori4n.mcmatchmakingsolo;

import java.util.UUID;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class Participant{
	public enum ParticipantRole{
		SPECTATOR,
		PLAYER,
		DISQUALIFIED
	}
	//only relevant for SPECTATOR 
	//if player disconnects SPECTATOR can become PLAYER if this bool is flase; 
	public boolean roleChosen = false;

	private final UUID uuid;

	private boolean alive;

	private ParticipantRole role;

	private int placement;
	
	private int kills;

}
