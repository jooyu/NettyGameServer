package cpgame.demo.core.constant;

/**
 * 响应消息的状态码
 * 
 * @author 0x737263
 * 
 */
public enum StatusCode {
	
	SUCCESS(0, "success", "success"),

	SERVER_ERROR(101000, "服务器错误", "服务器发生致命错误"),

	API_NOT_FOUND(101001, "Request Api Not Found", "请求接口不存在"),

	HTTP_METHOD_NOT_SUPPORTED(101002, "Http Method Is Not Suported For This Request", "请求的Http Method不支持，请检查是否选择了正确的Post/Get方式"),

	DB_EXCEPTION(101003, "服务器错误", "db异常"),

	MQ_EXCEPTION(101004, "服务器错误", "连接MQ失败"),

	// -------------------------------------------------------------------

	REQUEST_PARAMS_ERROR(201000, "缺少必要参数 %s", "参数数量或格式不符合接口要求"),

	LOGIN_FAIL(201001, "登陆失败", "渠道校验不通过导致的登陆失败"),

	ACCOUNT_BAN(201002, "账号已被封禁", "账号因违规被封停导致的禁止登陆"),

	ORDER_NOT_EXIST(201003, "order does not exist", "订单不存在"),

	GET_GAME_CONFIG_FAIL(201004, "获取游戏配置失败", "获取游戏配置失败"),

	CHANNEL_VERSION_ERROR(201005, "无法获取渠道 %s 快速接入 %s 版本", "无法获取快速接入的版本信息"),

	PARAMS_FORMAT_ERROR(201006, "参数 %s 不符合要求", "存在参数但是不符合系统要求"),

	PARAMS_MISSING(201007, "缺少必要渠道参数 %s", "没有配置相应的渠道参数或命名不对"),

	GET_GAME_INFO_FAIL(201008, "无法获取游戏信息", "获取游戏的基本信息失败"),

	ILLEGAL_USER(201009, "非法用户", "openid不合法等获取不到用户uid"),

	TOKEN_FAIL(201010, "token失效", "登陆的token 信息已经失效"),

	CHANNEL_NOT_FOUND(201011, "渠道不存在", "获取不到请求指定的渠道信息"),

	CREATE_ORDER_FAIL(201012, "订单创建失败", "创建订单失败"),

	AUTH_SIGN_FAIL(201013, "无权访问", "接口验签失败"),

	AUTH_SIGN_MISMATCHING(201014, "无权访问", "接口验签与服务端不一致"),

	LOGIN_SIGN_ERROR(201015, "sign error", "登陆回调验签失败"),

	OPEN_ID_MISMATCHING(201016, "Openid mismatch", "登陆回调提供的openid不一致"),

	SESSION_INVALID(201017, "sessionid Invalid", "登陆校验的sessionid不存在"),;

	private int code;
	private String info;
	private String desc;

	StatusCode(int code, String info, String desc) {
		this.code = code;
		this.info = info;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}

	public String getDesc() {
		return desc;
	}

}