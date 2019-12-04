package cn.aerotop.acquisition.beidou.util;

import io.netty.buffer.ByteBuf;

public class ToolsUtils {

	/**
	 * 整数转bytes，例如：337364=>[-44, 37, 5, 0]
	 * @param num long整形数
	 * @param length 字节数组长度
	 * @return bytes
	 */
	public static byte[] getLong2Bytes(long num, int length){
		byte[] b = new byte[length];
		for(int i=0; i<length; i++){
			int weiyi = i*8;
			if(i==0){
				b[i] = (byte) (num & 0xFF);
			}else{
				b[i] = (byte) ((num >> weiyi) & 0xFF);
			}
		}
		return b;
	}
	
	/**
	 * 计算校验和
	 * 计算byte数组异或的结果
	 * 
	 * @param data byte数组
	 * @return 校验和
	 */
	public static int getXor(byte[] data){
		int temp = data[0];              // 此处首位取1是因为本协议中第一个数据不参数异或校验，转为int防止结果出现溢出变成负数

        for (int i = 1; i < data.length; i++) {
            //int preTemp = temp;
            int iData;
            if (data[i] < 0) {
                iData = data[i] & 0xff;      // 变为正数计算
            } else {
                iData = data[i];
            }
            if (temp < 0) {
                temp = temp & 0xff;          // 变为正数
            }
            temp ^= iData;

            //System.out.println(preTemp + "异或" + iData + "=" + temp);
        }
        //System.out.println(temp);

        return temp;

	}
	/**
	 * 将16进制字符串变成byte数组
	 * 
	 * @param inHex 16进制字符串
	 * @return byte[] 
	 */
	public static byte[] hexToByteArray(String inHex){  
	    int hexlen = inHex.length();  
	    byte[] result;  
	    if (hexlen % 2 == 1){  
	        //奇数  
	        hexlen++;  
	        result = new byte[(hexlen/2)];  
	        inHex="0"+inHex;  
	    }else {  
	        //偶数  
	        result = new byte[(hexlen/2)];  
	    }  
	    int j=0;  
	    for (int i = 0; i < hexlen; i+=2){  
	        result[j]=hexToByte(inHex.substring(i,i+2));  
	        j++;  
	    }  
	    return result;   
	}
	
	/**
	 * 将单个16进制字符串变成byte
	 * @param inHex
	 * @return
	 */
	private static byte hexToByte(String inHex){  
	   return (byte)Integer.parseInt(inHex,16);  
	}
	//String字符串转byte数组
	public static byte[] stringToByte(String str) {
		byte[] bt=str.getBytes();
		return bt;
	}

	//将字符转换成ASCLL码
	public static int charToAscll(char c) {
		int b=c;
		return b;
	}
	
	//将ASCLL码转换成字符
	public static char AscllToChar(int i) {
		char c=(char) i;
		return c;
	}
	
	//将16进制转换成10进制
	public static int hexToInt(String hex) {
		int i = Integer.parseInt(hex, 16);
		return i;
	}
	
	//将字符串转换成ASCLL码
	public static String stringToAscll(String str) {
		char[] strs=str.toCharArray();
		String result="";
		for(int i=0;i<strs.length;i++) {
			int a=charToAscll(strs[i]);
			result +=a;
		}
		//System.out.println(result);
		return result;
	}
	
	//10进制int转16进制String
	private static String intToHex(int n) {
        StringBuilder sb = new StringBuilder(8);
        String a;
        char []b = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        while(n != 0){
            sb = sb.append(b[n%16]);
            n = n/16;            
        }
        a = sb.reverse().toString();
        return a;
    }
	//16进制转单精度浮点数
	public static float hexToFloat(String hex) {
		Float value = Float.intBitsToFloat(Integer.valueOf(hex.trim(), 16)); 
        //System.out.println(value); 
        return value;
	}
	
	/**
	 * 字符串转日期
	 * @param str 191203170506
	 * @return
	 */
	public static String stringToDate(String str) {
		String result="20"+str.substring(0,2)+"-"+str.substring(2,4)+"-"+str.substring(4,6)+" "+str.substring(6,8)+":"+str.substring(8,10)+":"+str.substring(10,12);
		return result;
	}
	

	/**
	 * 根据传入的解析后报文str，checknum，判断校验和是否正确
	 * @param str 报文
	 * @param cs 校验和
	 * @return boolean
	 */
	public static boolean checksum(String str, String cs){
		String result=intToHex(getXor(stringToByte(str)));
		if(result.equals(cs))
			return true;
		return false;
	}

	
	/**
	 * 解析电文内容
	 * @param content 电文内容
	 */
	public static void analysisContent(String content) {
		String sign=content.substring(0,2);//首字母A4
		String functionCode=content.substring(2,4);//功能码
		String Initials=content.substring(4,6);//系统站号首字母
		int i=hexToInt(Initials);
		char c=AscllToChar(i);
		String systemId=content.substring(6,12);
		systemId=c+systemId;//系统站号
		String stationType=content.substring(12,14);//站点类型 02-代表高压调压站
		String dataSendTime=content.substring(14,26);//数据发送时间
		dataSendTime=stringToDate(dataSendTime);
		String exback=content.substring(26,28);//00表示无控制指令执行
		String state=content.substring(28,36);//状态量监测
		String totalNum=content.substring(36,38);//总口个数
		String pressures=content.substring(38,38+4*(Integer.parseInt(totalNum)));
		for(int j=0;j+4<=pressures.length();j=j+4) {
			String pressure=pressures.substring(j, j+4);
			int pre=hexToInt(pressure);
			//System.out.println(pre);
		}
		for(int a=38+4*(Integer.parseInt(totalNum));a+18<=content.length()-2;a=a+18) {
			String flow=content.substring(a,a+18);
			String flowId=flow.substring(0,2);
			String flowIns=flow.substring(2,10);
			String flowTotal=flow.substring(10, 18);
			//瞬时流量和总累流量转化成单精度浮点数
			float flowIn=hexToFloat(flowIns);
			float flowTol=hexToFloat(flowTotal);
			System.out.println(flowId);
			System.out.println(flowIn);
			System.out.println(flowTol);
		}
	}

	public static void main(String[] args) {
		/*String str="BDTXR,1,0337366,1,,0002F200000039CED441A6843D459067114DAB1EB8BE05DF2C41B00000000012AC4CC70A3DC10FCB7341A50F2E46907A0B4D510000000000000000000000000000000000000000000000000000DD";
		//String str1="42445458522c312c303333373336342c322c2c4134106E0001000219110812071400555555550804D2162E23340D8000000000000000000542F6E9790000000006435E553F0000000014";
		//String str1=stringToAscll(str);
		byte[] bt=stringToByte(str);
		int temp=getXor(bt);
		String result=intToHex(temp);
		System.out.println(result);*/
		//hexToFloat("4987A238");
		String content="A4106E00010002191203170506005555555508045708AE0D05115C15B31A0A1E6122B8014133851F4987A238024205C28F4A07A23803425EA3D74A4B735404429BC28F4A87A238E0";
		analysisContent(content);
	}
}
