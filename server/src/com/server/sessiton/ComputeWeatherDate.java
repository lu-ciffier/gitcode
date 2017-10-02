package com.server.sessiton;

public class ComputeWeatherDate {

	private static int index;//传感器类型所在的索引
	private static String type;//传感器类型
	public static void  compute(String data) {
		index = 44;
		while (index < data.length()) {
			type = data.substring(index, index + 2);
			System.out.println(type);
			if (type.equals("02")) {//内部传感器14字节
				String vRefl = data.substring(index + 4, index + 6);
				String vRefh = data.substring(index + 6, index + 8);
				String vRef = vRefh + vRefl;
				float vref = Integer.parseInt(vRef, 16);

				String batteryV = data.substring(index + 8, index + 10);
				float batteryx = Integer.parseInt(batteryV, 16);
				float batteryv = (float) (8 * 1.225 * (batteryx / vref));

				String solarV = data.substring(index + 10, index + 12);
				float solarx = Integer.parseInt(solarV, 16);
				float solarv = (float) (17.2 * 1.225 * (solarx / vref));

				String internalTempl = data.substring(index + 12, index + 14);
				String internalTemph = data.substring(index + 14, index + 16);
				String internalTemp = internalTemph + internalTempl;
				float interx = Integer.parseInt(internalTemp, 16);
				float internaltemp = (float) (1 / (0.001307050 + 0.000214381 * Math
						.log(10000 * (1024 - interx) / interx) + 0.000000093 * Math
						.log(Math.pow((10000 * (1024 - interx) / interx), 3))) - 273.15);

				String externalV = data.substring(index + 20, index + 22);
				float externalvx = Integer.parseInt(externalV, 16);
				float externalv = (float) (17.2 * 1.225 * (externalvx / vref));

				System.out.println(batteryv);
				System.out.println(solarv);
				System.out.println(internaltemp);
				System.out.println(externalv);			
				index = index + 28;
				continue;
			}
			if(type.equals("11")){//环境温湿度传感器9字节
				String temperturexl = data.substring(index+4, index+6);
				String temperturexh = data.substring(index+6, index+8);
				String temperturex = temperturexh + temperturexl;
				float x = Integer.parseInt(temperturex, 16);
				float temperture = (float) (-39.6 + 0.01*x);
				
				String humidityyl  = data.substring(index+8, index+10);
				String humidityyh  = data.substring(index+10, index+12);
				String humidityy  = humidityyh + humidityyl;
				float y = Integer.parseInt(humidityy, 16);
				float humidity =  (float) ((-39.6 + 0.01*x - 25.0)*(0.01 + 0.00008*y) 
						- 4.0 + 0.0405 * y - 0.0000028 * y * y);
				System.out.println(temperture);
				System.out.println(humidity);
				index = index + 18;
				continue;
			}
			if(type.equals("12")){//树叶湿度传感器11字节
				index = index + 22;
				continue;
			}
			if(type.equals("14")){//土壤水分传感器11字节
				index = index + 22;
				continue;
			}
			if(type.equals("15")){//太阳辐射传感器11字节
				
				String readcl = data.substring(index+4, index+6);
				String readch = data.substring(index+6, index+8);
				String readc = readch + readcl;
				String exadcl = data.substring(index+8, index+10);
				String exadch = data.substring(index+10, index+12);
				String exadc = exadch + exadcl;				
				float reADC = Integer.parseInt(readc, 16);
				float exADC = Integer.parseInt(exadc, 16);
				float E1ExcitationV = (float) (1.225*2*exADC/reADC);
				
				String solaradcl = data.substring(index+12, index+14);
				String solaradch = data.substring(index+14, index+16);
				String solaradc = solaradch + solaradcl;
				float solarADC = Integer.parseInt(solaradc, 16);
				float solarRadiation =  (float) (1225*solarADC/reADC/1.67);
				
				System.out.println(E1ExcitationV);
				System.out.println(solarRadiation);
				index = index + 22;
				continue;
			}
			if(type.equals("16")){//气象站35字节
				index = index + 70;
				continue;
			}
			if(type.equals("17")){//土壤湿度传感器7字节
				String soilMoisturel = data.substring(index+4, index+6);
				String soilMoistureh = data.substring(index+6, index+8);
				String soilMoisture = soilMoistureh + soilMoisturel;
				float soilmoisturex = Integer.parseInt(soilMoisture, 16);
				float soilmoisture = (float) (7.407*10.0*soilmoisturex/
						(1024-soilmoisturex)-3.704);
				
				System.out.println(soilmoisture);
				index = index + 14;
				continue;
			}
			break;
		}
	}
	public static void main(String[] args) {
		String data = "7E 42 7D 5E 00 0B 53 50 00 00 08 00 00 00 34 01 E0 00 00 0D 00 00 02 01 A0 01 A5 05 F1 01 0D 03 02 08 01 00 11 00 1A 19 14 09 22 03 00 16 00 00 00 00 00 00 00 60 00 60 00 00 00 00 00 0F 00 FB 18 C2 08 06 00 FE 03 8B 01 0A 26 F1 00 0A 04 00 15 00 A0 01 DC 01 00 00 6F 7D 5D 7E";
		data = data.replaceAll(" ", "");
		compute(data);
	}
}
