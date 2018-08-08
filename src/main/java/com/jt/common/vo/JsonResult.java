package com.jt.common.vo;
import java.io.Serializable;
//有的公司叫SysResult/Result/R
public class JsonResult implements Serializable {
	private static final long serialVersionUID = 6205026608497559648L;
	/**状态码*/
	private Integer state=1;//1表示SUCCESS,0表示ERROR
	/**状态码对应的消息*/
	private String message="ok";
	/**服务端返回给客户端的数据*/
	private Object data;//这里的参数名必须叫data,因为后面用的第三方树插件指定的名字
	public JsonResult(String message) {
		this.message=message;
	}
	/**一般查询时调用，封装查询结果*/
	public JsonResult(Object data){
		this.data=data;
	}
	/**出现异常时时调用*/
	public JsonResult(Throwable e){
		this.state=0;//表示代表错误
		this.message=e.getMessage();
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
