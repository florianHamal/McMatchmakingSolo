package at.flori4n.mcmatchmakingsolo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.flori4n.mcmatchmakingsolo.Participant.ParticipantRole;
import at.flori4n.mcmatchmakingsolo.states.lobby.LobbyState;
import lombok.Getter;
import lombok.Setter;


public class ParticipantService {
	private static ParticipantService INSTANCE;

	private GameData gameData = GameData.getInstance();
	
	@Getter
	private List<Participant> participants = new ArrayList<Participant>();
	
	private ParticipantService() {
	}



	public void onPlayerJoin(Player player) {
		Participant participant = getOrCreateParticipant(player);

		if (gameData.getFixedPlayers().contains(player.getName())) {
			participant.setRole(Participant.ParticipantRole.PLAYER);
		} else if (gameData.getFixedSpectators().contains(player.getName())) {
			participant.setRole(Participant.ParticipantRole.SPECTATOR);
		}
		// default role is Player
		changePlayerRole(player, ParticipantRole.PLAYER);
	}
	
	//todo : Move onPlayerLeave and on Player join into the listeners

	public void onPlayerLeave(Player player) {


		}else{
		}
	 	
	}

	public void changePlayerRole(Player player, ParticipantRole role) {
		if (!gameData.isAllowRoleChanging()) {
			player.sendMessage(
					"Das ändern deiner Rolle in dieser Runde nicht erlaubt. Du startest mit einer fixen Rolle");
			return;
		}
		if (role == ParticipantRole.PLAYER && getPlayerCount() >= gameData.getMaxPlayers()) {
			player.sendMessage("Zu viele Spieler im Game. Du bist SPECTATOR");
			getOrCreateParticipant(player).setRole(ParticipantRole.SPECTATOR);
			return;
		}
		Participant participant = getOrCreateParticipant(player);
		participant.setRole(role); 
		if (role == ParticipantRole.SPECTATOR)
			participant.setRoleChosen(true);
	}

	public Participant getOrCreateParticipant(UUID uuid) {
		return getParticipant(uuid).orElseGet(() -> {
			Participant p = new Participant(uuid);
			participants.add(p);
			return p;
		});
	}

	public Participant getOrCreateParticipant(Player player) {
		return getOrCreateParticipant(player.getUniqueId());
	}

	public Optional<Participant> getParticipant(UUID uuid) {
		return participants.stream().filter(participant -> participant.getUuid().equals(uuid)).findFirst();
	}

	public Optional<Participant> getParticipant(Player player) {
		return getParticipant(player.getUniqueId());
	}

	public List<Participant> getPlayers() {
		return participants.stream().filter(p -> p.getRole() == ParticipantRole.PLAYER)
				.collect(Collectors.toList());
	};

	public int getPlayerCount() {
		return (int) participants.stream().filter(p -> p.getRole() == ParticipantRole.PLAYER).count();
	}

	public static ParticipantService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ParticipantService();
		return INSTANCE;

	}

}
