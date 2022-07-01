package common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

import function.GameDataContainer;
import function.map.MapChip;
import function.skill.Skill;
import function.unit.Job;
import function.unit.Race;
import function.unit.Unit;
import javafx.application.Platform;

/**
 * このクラスは全体で使うものをまとめたものです。
 * @author 樹麗
 *
 */
@SuppressWarnings("unchecked")
public class SRPGCommons {
	private static URL url;
	private static ObjectMapper mapper;
	private static GameDataContainer<Job> job_container;
	private static GameDataContainer<Race> race_container;
	private static GameDataContainer<Unit> unit_container;
	private static GameDataContainer<Skill> skill_container;
	private static GameDataContainer<MapChip> map_chip_container;
	static{
		try {
			url = new URL("file:/" + Paths.get("", "data").toAbsolutePath().toString());
		} catch (MalformedURLException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try{
			skill_container = mapper.readValue(new File("./data/skill/skill.dat"), GameDataContainer.class);
			job_container = mapper.readValue(new File("./data/unit/job.dat"), GameDataContainer.class);
			race_container = mapper.readValue(new File("./data/unit/race.dat"), GameDataContainer.class);
			unit_container = mapper.readValue(new File("./data/unit/unit.dat"), GameDataContainer.class);
			map_chip_container = mapper.readValue(new File("./data/map/mapchip.dat"), GameDataContainer.class);
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
	private SRPGCommons(){}

	/**
	 * @param id　ユニットID
	 * @return 指定されたIDを持つユニット
	 */
	public static final Unit getUnit(String id){ return unit_container.getValue(id); }
	/**
	 * @return　ユニットリストを返す
	 */
	public static final Collection<Unit> getUnitList() { return Collections.unmodifiableCollection(unit_container.getData().values()); }
	/**
	 * @return　自身のユニットのリストを返す
	 */
	public static final Collection<Unit> getMyUnitList() { return Collections.unmodifiableCollection(unit_container.getData().values()); }
	/**
	 * @param id　種族ID
	 * @return 指定されたIDを持つ種族
	 */
	public static final Race getRace(String id){ return race_container.getValue(id); }
	/**
	 * @return　種族リストを返す
	 */
	public static final Collection<Race> getRaceList() { return Collections.unmodifiableCollection(race_container.getData().values()); }
	/**
	 * @param id　職業ID
	 * @return 指定されたIDを持つ職業
	 */
	public static final Job getJob(String id){ return job_container.getValue(id); }
	/**
	 * @return　職業リストを返す
	 */
	public static final Collection<Job> getJobList() { return Collections.unmodifiableCollection(job_container.getData().values()); }
	/**
	 * @param id　スキルID
	 * @return 指定されたIDを持つスキル
	 */
	public static final Skill getSkill(String id){ return skill_container.getValue(id); }
	/**
	 * @return　スキルリストを返す
	 */
	public static final Collection<Skill> getSkillList() { return Collections.unmodifiableCollection(skill_container.getData().values()); }
	/**
	 * @param id　マップチップID
	 * @return 指定されたIDを持つマップチップ
	 */
	public static final MapChip getMapChip(String id) { return map_chip_container.getValue(id); }
	/**
	 * @return マップチップリストを返す
	 */
	public static final Collection<MapChip> getMapChipList() { return Collections.unmodifiableCollection(map_chip_container.getData().values()); }
	/**
	 * tmpファイルを作成する
	 */
	public static final void createTmpFile() {
		String dir = "./data/tmp";
		try {
			if(!Files.isDirectory(Paths.get(dir))) {
				Files.createDirectories(Paths.get(dir));
			}
			if (!Files.exists(Paths.get(dir, "/resource.dat"))) {
				Files.createFile(Paths.get(dir, "/resource.dat"));
			}
			Files.write(Paths.get(dir, "/resource.dat"), "10000".getBytes(Charset.forName("UTF-8")));
			if (!Files.exists(Paths.get(dir, "/my_unit.dat"))) {
				Files.createFile(Paths.get(dir, "/my_unit.dat"));
			}
			saveData(Paths.get(dir, "/my_unit.dat").toFile(),
					new GameDataContainer<Unit>(new HashMap<String, Unit>()));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	/**
	 * @param file 読み込むファイル
	 * @param target_class どのクラスとして読み込む
	 * @return 読みこんんだクラス
	 * 指定された場所にあるJSONファイルを指定されたClassとして読み込み返す
	 */
	public static final Object getData(File file, Class<?> target_class){
		try {
			return mapper.readValue(file, target_class);
		} catch (JsonParseException e) {
			// TODO 自動生成された catch ブロック
			throw new IllegalArgumentException();
		} catch (JsonMappingException e) {
			// TODO 自動生成された catch ブロック
			throw new IllegalArgumentException();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			throw new IllegalArgumentException();
		}
	}
	/**
	 * @param file セーブ場所
	 * @param object セーブするオブジェクト
	 * 指定された場所に渡されたオブジェクトをJSON形式で保存する
	 */
	public static final void saveData(File file, Object object){
		PrintWriter pw = null;
		try {
			String json = mapper.writeValueAsString(object);
			pw = new PrintWriter(
					new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(file), "UTF-8")));
			pw.println(json);
			pw.close();
			System.setProperty("file.encoding", "UTF-8");
		} catch (JsonParseException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (JsonMappingException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} finally{
			if(pw != null){
				pw.close();
			}
		}
	}
	/**
	 * @param runnable スレッド
	 * @throws InterruptedException
	 * @throws InvocationTargetException
	 * スレッドの待ち合わせ用
	 */
	public static void runAndWait(Runnable runnable)
			throws InterruptedException, InvocationTargetException {

		if(Platform.isFxApplicationThread()) {
			throw new Error("Cannot call runAndWait from the FX Application Thread");
		}
		Throwable[] throwable = new Throwable[1];
		CountDownLatch latch = new CountDownLatch(1);
		Platform.runLater(() -> {
			try {
				runnable.run();
			} catch(Throwable t) {
				throwable[0] = t;
			} finally {
				latch.countDown();
			}
		});
		latch.await();
		if(throwable[0] != null) {
			throw new InvocationTargetException(throwable[0]);
		}
	}
	public static final URL getURL(){ return url; }

}
