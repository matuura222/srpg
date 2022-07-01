package function;

import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

import function.map.GameMapData;
import function.map.MapChip;
import function.map.Square;
import function.skill.AttackSkill;
import function.skill.ConditionSkill;
import function.skill.SummonSkill;
import function.skill.SupportSkill;
import function.unit.Job;
import function.unit.Race;
import function.unit.Unit;

/**
 *　GameDataのJSONの型解決用クラス
 * @author 樹麗
 */
public class GameDataTypeIdResolver implements TypeIdResolver {
	JavaType super_type;
	@Override
	public void init(JavaType base_type) {
		super_type = base_type;
	}
	/* (非 Javadoc)
	 * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#idFromBaseType()
	 */
	@Override
	public String idFromBaseType() {
		throw new UnsupportedOperationException();
	}

	/* (非 Javadoc)
	 * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#idFromValue(java.lang.Object)
	 */
	@Override
	public String idFromValue(Object value) {
		return idFromValueAndType(value, value.getClass());
	}

	/* (非 Javadoc)
	 * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#idFromValueAndType(java.lang.Object, java.lang.Class)
	 */
	@Override
	public String idFromValueAndType(Object value, Class<?> suggested_type) {
		if(Job.class.isAssignableFrom(suggested_type)){
			return "Job";
		}else if(Race.class.isAssignableFrom(suggested_type)){
			return "Race";
		}else if(Unit.class.isAssignableFrom(suggested_type)){
			return "Unit";
		}else if(AttackSkill.class.isAssignableFrom(suggested_type)){
			return "AttackSkill";
		}else if(SummonSkill.class.isAssignableFrom(suggested_type)){
			return "SummonSkill";
		}else if(SupportSkill.class.isAssignableFrom(suggested_type)){
			return "SupportSkill";
		}else if(ConditionSkill.class.isAssignableFrom(suggested_type)){
			return "ConditionSkill";
		}else if(Square.class.isAssignableFrom(suggested_type)){
			return "Square";
		}else if(MapChip.class.isAssignableFrom(suggested_type)){
			return "MapChip";
		}else if(GameMapData.class.isAssignableFrom(suggested_type)){
			return "GameMapData";
		}
		throw new IllegalArgumentException();
	}
	/* (非 Javadoc)
	 * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#typeFromId(com.fasterxml.jackson.databind.DatabindContext, java.lang.String)
	 */
	@Override
	public JavaType typeFromId(DatabindContext context, String id){
		TypeFactory type_factory;
		if(context != null){
			type_factory = context.getTypeFactory();
		}else{
			type_factory = TypeFactory.defaultInstance();
		}
		if("Job".equals(id)){
			return type_factory.constructType(Job.class);
		}else if("Race".equals(id)){
			return type_factory.constructType(Race.class);
		}else if("Unit".equals(id)){
			return type_factory.constructType(Unit.class);
		}else if("AttackSkill".equals(id)){
			return type_factory.constructType(AttackSkill.class);
		}else if("SummonSkill".equals(id)){
			return type_factory.constructType(SummonSkill.class);
		}else if("SupportSkill".equals(id)){
			return type_factory.constructType(SupportSkill.class);
		}else if("ConditionSkill".equals(id)){
			return type_factory.constructType(ConditionSkill.class);
		}else if("Square".equals(id)){
			return type_factory.constructType(Square.class);
		}else if("MapChip".equals(id)){
			return type_factory.constructType(MapChip.class);
		}else if("GameMapData".equals(id)){
			return type_factory.constructType(GameMapData.class);
		}
		throw new IllegalArgumentException();
	}
	/* (非 Javadoc)
	 * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#getDescForKnownTypeIds()
	 */
	@Override
	public String getDescForKnownTypeIds() {
		return null;
	}

	/* (非 Javadoc)
	 * @see com.fasterxml.jackson.databind.jsontype.TypeIdResolver#getMechanism()
	 */
	@Override
	public Id getMechanism() {
		// TODO 自動生成されたメソッド・スタブ
		return Id.NAME;
	}

}
