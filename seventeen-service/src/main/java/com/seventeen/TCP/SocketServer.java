package com.seventeen.TCP;

import com.seventeen.service.impl.SeOrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @function Socket服务
 * @author 凌虚风
 * @Time 2016-12-31 21:04:01
 */

public class SocketServer {

	public static final int PORT = 50001;//要监听的端口

	/**
	 * @Description
     * @function 当前时间格式化输出
	 * @return 格式化后的时间
	 */
	static String getTime() {
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:s]");
		return f.format(new Date());
	}
	public void run() throws IOException {
		
		ServerSocket ss = new ServerSocket(PORT);
		InetAddress ia = InetAddress.getByName(null);
		System.out.println("服务端@" + ia + " 已启动!");
		
		try {
			
			while (true) {

				Socket s = ss.accept();//listen PORT;
				System.out.println("已开始监听:"+ss.getLocalPort()+"端口");
				try {
					new ServerOne(s);
				} catch (IOException e) {
					s.close();
				}
			}
		} finally {
			ss.close();
			System.out.println("服务端已停止!");
		}
	}
}

class ServerOne extends Thread {
	private Socket s;
	private InputStream in;
	private OutputStream out;

    @Autowired
    private SeOrderServiceImpl seOrderServiceImpl;

    byte[] buf = new byte[1024*2];//缓冲区大小
	public ServerOne(Socket s) throws IOException {
		this.s = s;
		in=s.getInputStream(); 
		out = s.getOutputStream();
		start();
	}

	public void run() {
        try {
			while (true) {
				
			    in.read(buf);//读取数据 
			    
				String hexStr=HexTool.bytesToHexString(buf);//将字节数组转成十六进制字符串
				
				String commandStr=hexStr.substring(0,8);//截取到命令
				
				
				if("55550601".equals(commandStr)){
					
					System.out.println("|【已收到控制板<服务器开门>结果通知】");
					//55550601:控制板向服务端发送开门结果通知指令
					
				}else if("55550701".equals(commandStr)){
					
					//55550601:控制板向服务端发送心跳指令
					
				}else if("55550801".equals(commandStr)){
					
					//0810:控制板向服务端上报刷身份证指令
					/**
					 * 通过校验位来验证数据是否合法
					 * 可供调用的算法函数XorCheck在HexTool类中
					 * 此处省略校验过程
					 */
					System.out.println(" -----------------------------------------");
					String bodyData=hexStr.substring(12, hexStr.length()-4);//截取到报文中的数据部分
					//System.out.println("|【解析数据包<设备hex_ID>】"+bodyData.substring(0, 16));
					System.out.println("|【解析数据包<设备返回码>】"+bodyData.substring(16, 18));
					System.out.println("|【解析数据包<设备端口号>】"+bodyData.substring(18, 20));
					System.out.println("|【解析数据包<设备方向>】"+bodyData.substring(20, 22));
					System.out.println("|【解析数据包<刷卡时间>】"+bodyData.substring(22, 36));
					
					String operResult=bodyData.substring(36, 38);//刷卡操作结果
					
					if("00".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x00<卡正常并开门>>】");
					}else if("01".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x01<卡正常设备时间错误码未没有开门>>】");
					}else if("02".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x02<卡正常有效时间失效未没有开门>>】");
					}else if("03".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x03<卡正常非工作时间未没有开门>>】");
					}else if("04".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x04<卡正常，电脑鉴权没有开门>>】");
					}else if("05".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x05<黑名单没开门>>】");
					}else if("06".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x06<持失卡没开门>>】");
					}else if("07".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x07<无效卡没开门>>】");
					}else if("08".equals(operResult)){
						System.out.println("|【解析数据包<刷卡结果0x08<无鉴权开门>>】");
					}
					String cardType=bodyData.substring(38, 40);//截取卡片类型
					
					if("00".equals(cardType)){
						System.out.println("|【解析数据包<卡片类型0x00<IC卡>>】");
					}else if("01".equals(cardType)){
						System.out.println("|【解析数据包<卡片类型0x01<身份证>>】");
					}else if("02".equals(cardType)){
						System.out.println("|【解析数据包<卡片类型0x02<UID>>】");
					}else if("03".equals(cardType)){
						System.out.println("|【解析数据包<卡片类型0x03<二维码>>】");
					}else if("04".equals(cardType)){
						System.out.println("|【解析数据包<卡片类型0x04<IC数据模块>>】");
					}
					
					
					byte[] deviceID=new byte[8];
					System.arraycopy(buf, 6, deviceID,0,8);
					System.out.println("|【解析数据包<设备ID>】"+new String (deviceID));
					
					byte[] manName=new byte[30];
					System.arraycopy(buf, 42, manName,0,30);
					System.out.println("|【解析数据包<刷卡人姓名>】"+new String (manName,"UTF-16LE"));
					
					byte[] sex=new byte[2];
					System.arraycopy(buf, 72, sex,0,2);
					System.out.print("|【解析数据包<刷卡人性别>】");
					System.out.println(""+new String(sex,"UTF-16LE").equals("1") != null?"男":"女");
					
					byte[] nation=new byte[4];
					System.arraycopy(buf, 74,nation,0,4);
					System.out.println("|【解析数据包<刷卡人民族>】"+new String(nation,"UTF-16LE"));
					
					byte[] birthday=new byte[16];
					System.arraycopy(buf, 78,birthday,0,16);
					System.out.println("|【解析数据包<刷卡人生日>】"+new String(birthday,"UTF-16LE"));
					
					byte[] address=new byte[70];
					System.arraycopy(buf, 94,address,0,70);
					System.out.println("|【解析数据包<刷卡人住址>】"+new String(address,"UTF-16LE"));
					
					
					byte[] cardNo=new byte[36];
					System.arraycopy(buf, 164, cardNo,0,36);
					System.out.println("|【解析数据包<刷卡人身份证号>】"+new String (cardNo,"UTF-16LE"));
					if(operResult.equals("00")){
                        seOrderServiceImpl.upgradeLockCron(cardNo.toString());
                    }


					byte[] signOffice=new byte[30];
					System.arraycopy(buf, 200,signOffice,0,30);
					System.out.println("|【解析数据包<身份证签发机关>】"+new String(signOffice,"UTF-16LE"));
					
					byte[] validityStartAt=new byte[16];
					System.arraycopy(buf, 230,validityStartAt,0,16);
					System.out.println("|【解析数据包<证件有效期起始日期>】"+new String(validityStartAt,"UTF-16LE"));
					
					byte[] validityEndAt=new byte[16];
					System.arraycopy(buf, 246,validityEndAt,0,16);
					System.out.println("|【解析数据包<证件有效期截止日期>】"+new String(validityEndAt,"UTF-16LE"));
					
					byte[] new_address=new byte[36];
					System.arraycopy(buf, 262,new_address,0,36);
					System.out.println("|【解析数据包<刷卡人最新住址>】"+new String(new_address,"UTF-16LE"));
				 
					
					/**
					 * 组装开门命令
					 * 命令结构=引导码（16进制）+命令（16 进制）+数据长度（short 型）+数据（16 进制）+校验位+结束码
					 * 命令格式=
					 * AAAA+0601+1000+HexTool.bytesToHexString(deviceID)+[继电器控制]
					 * +HexTool.XorCheck(命令+数据长度+数据)+0D
					 * 
					 * 继电器控制说明:
					 * 报文格式：X,Y (X代表延时多少毫秒后开启门,Y代表动作，01->开门 00->维持门原状态)
					 * 只开第1道门,延时14*100毫秒的报文示例：
					 * 门1--->0E 01
					 * 门2--->00 00
					 * 门3--->00 00
					 * 门4--->00 00
					 * 最终拼接的十六进制字符串为:0E01000000000000
					 */
					
					String head="AAAA06011000";//引导码+长度
					
					String A="0E01",B="0000",C="0000",D="0000";
					
					String hex_data=HexTool.bytesToHexString(deviceID)+A+B+C+D;
					
					String checkData="0601"+"1000"+hex_data;//参与校验的数据报文
					
					byte sign=HexTool.XorCheck(HexTool.hexStringToBytes(checkData));
					
					byte[] sginByte={sign};//将字节装入数组

					System.out.println("|【组装开门指令<计算得到校验值>】"+sign);
					
					String hexOpendoorCommand=head+hex_data+HexTool.bytesToHexString(sginByte).toUpperCase()+"0D";
					
					System.out.println("|【组装开门指令<最终的十六进制开门指令串>】"+hexOpendoorCommand);

					out.write(HexTool.hexStringToBytes(hexOpendoorCommand));
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException e) {
			}
		}
	}
}