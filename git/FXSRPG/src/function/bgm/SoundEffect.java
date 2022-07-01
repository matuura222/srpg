package function.bgm;

import java.util.HashMap;
import java.util.Map;

import common.SRPGCommons;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

/**
 * このクラスは効果音を制御しているオブジェクトです。
 * @author 樹麗
 *
 */
public class SoundEffect {
	private static Map<String, MediaPlayer> id_to_sound_effect = new HashMap<String, MediaPlayer>();
	private SoundEffect(){}
	/**
	 * @param sound_effect_name　効果音名
	 * 指定された効果音を流します。
	 * 無かった場合流れません。
	 */
	public static void play(String sound_effect_name){
		if(sound_effect_name == null || sound_effect_name.equals("")){ return; }
		MediaPlayer player = id_to_sound_effect.get(sound_effect_name);
		try{
			if(player == null){
				Media media = new Media(SRPGCommons.getURL() + "/bgm/" + sound_effect_name);
				MediaPlayer p = new MediaPlayer(media);
				p.setOnEndOfMedia(() -> p.stop());
				id_to_sound_effect.put(sound_effect_name, p);
				player = p;
			}
			if(player.getStatus() == Status.PLAYING){
				player.stop();
			}
			player.play();
		}catch(MediaException e){
			throw new IllegalArgumentException(SRPGCommons.getURL() + "/bgm/" + sound_effect_name + "がありません", e);
		}
	}
	/**
	 * @param sound_effect_name 効果音名
	 * 指定された効果音を止めます。
	 * 無かった場合何も起きません。
	 */
	public static void dispose(String sound_effect_name){
		id_to_sound_effect.get(sound_effect_name).dispose();
		id_to_sound_effect.remove(sound_effect_name);
	}
}
