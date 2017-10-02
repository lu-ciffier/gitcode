package com.server.jopo;

import java.sql.Timestamp;
import java.util.Date;


/**
 * @author lucyf
 * @version 2017.5.10
 * Weather entity. @author MyEclipse Persistence Tools
 */

public class Weather  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
     private String pointNum;
     private Float temperature;
     private Integer humidity;
     private Float windSpeed;
     private Float windDir;
     private Float solarRadiation;
     private Float batteryV;
     private Float solarV;
     private Timestamp date;


    // Constructors

    /** default constructor */
    public Weather() {
    }

    
    /** full constructor */
    public Weather(String pointNum, Float temperature, Integer humidity, Float windSpeed, Float windDir, Float solarRadiation, Float batteryV, Float solarV, Timestamp date) {
        this.pointNum = pointNum;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.windDir = windDir;
        this.solarRadiation = solarRadiation;
        this.batteryV = batteryV;
        this.solarV = solarV;
        this.date = date;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPointNum() {
        return this.pointNum;
    }
    
    public void setPointNum(String pointNum) {
        this.pointNum = pointNum;
    }

    public Float getTemperature() {
        return this.temperature;
    }
    
    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Integer getHumidity() {
        return this.humidity;
    }
    
    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Float getWindSpeed() {
        return this.windSpeed;
    }
    
    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Float getWindDir() {
        return this.windDir;
    }
    
    public void setWindDir(Float windDir) {
        this.windDir = windDir;
    }

    public Float getSolarRadiation() {
        return this.solarRadiation;
    }
    
    public void setSolarRadiation(Float solarRadiation) {
        this.solarRadiation = solarRadiation;
    }

    public Float getBatteryV() {
        return this.batteryV;
    }
    
    public void setBatteryV(Float batteryV) {
        this.batteryV = batteryV;
    }

    public Float getSolarV() {
        return this.solarV;
    }
    
    public void setSolarV(Float solarV) {
        this.solarV = solarV;
    }

    public Timestamp getDate() {
        return this.date;
    }
    
    public void setDate(Timestamp date) {
        this.date = date;
    }
   
    private static int index;//传感器类型所在的索引
	private static String type;//传感器类型
	public static Weather  compute(String data) {
		Weather weather = null;
		String pointNum = data.substring(20,22);
		index = 44;
		while (index < data.length()) {
			type = data.substring(index, index + 2);
			System.out.println(type);
			if (type.equals("02")) {//内部传感器14字节
				weather = new Weather();
				// 电压单位：V
				String vRefl = data.substring(index + 4, index + 6);
				String vRefh = data.substring(index + 6, index + 8);
				String vRef = vRefh + vRefl;
				float vref = Integer.parseInt(vRef, 16);

				String batteryV = data.substring(index + 8, index + 10);
				float batteryx = Integer.parseInt(batteryV, 16);
				float batteryv = (float) (8 * 1.225 * (batteryx / vref));
				batteryv = (float)(Math.round(batteryv*100))/100;

				String solarV = data.substring(index + 10, index + 12);
				float solarx = Integer.parseInt(solarV, 16);
				float solarv = (float) (17.2 * 1.225 * (solarx / vref));
				solarv = (float)(Math.round(solarv*100))/100;

				/*String internalTempl = data.substring(index + 12, index + 14);
				String internalTemph = data.substring(index + 14, index + 16);
				String internalTemp = internalTemph + internalTempl;
				float interx = Integer.parseInt(internalTemp, 16);
				float internaltemp = (float) (1 / (0.001307050 + 0.000214381 * Math
						.log(10000 * (1024 - interx) / interx) + 0.000000093 * Math
						.log(Math.pow((10000 * (1024 - interx) / interx), 3))) - 273.15);

				String externalV = data.substring(index + 20, index + 22);
				float externalvx = Integer.parseInt(externalV, 16);
				float externalv = (float) (17.2 * 1.225 * (externalvx / vref));*/
							
				weather.setPointNum(pointNum);
				weather.setBatteryV(batteryv);
				weather.setSolarV(solarv);
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
//01001600 0000 0000 0000 9B01 9B01 0000 0000 0F00 5A18 9708 0600 FE03 8F01 7226 DD00 4E
			if(type.equals("16")){//气象站35字节
				//windSpeed and windDir 单位：km/h，度
				String windspeedl = data.substring(index+12, index+14);
				String windspeedh = data.substring(index+14, index+16);
				String windspeed = windspeedh + windspeedl;
				float winds = Integer.parseInt(windspeed, 16);
				String timercntl = data.substring(index+32, index+34);
				String timercnth = data.substring(index+34, index+36);
				String timercnt = timercnth + timercntl;
				float timer = Integer.parseInt(timercnt, 16);
				float windSpeed = (float)(2.25*(winds/(2 *timer))*1.609344);
				windSpeed = (float)(Math.round(windSpeed*100))/100;
				
				String winddirl = data.substring(index+20, index+22);
				String winddirh = data.substring(index+22, index+24);
				String winddir = winddirh + winddirl;
				float windd = Integer.parseInt(winddir, 16);
				float windDir = (360 *windd)/1023;
				windDir = (float)(Math.round(windDir*100))/100;
				
				weather.setWindSpeed(windSpeed);
				weather.setWindDir(windDir);
				
				
				//temperature and humidity 单位：摄氏度，100%
				String temperturexl = data.substring(index+36, index+38);
				String temperturexh = data.substring(index+38, index+40);
				String temperturex = temperturexh + temperturexl;
				float x = Integer.parseInt(temperturex, 16);
				float temperture = (float) (-39.75 + 0.01*x);
				temperture = (float)(Math.round(temperture*100))/100;
				
				String humidityyl  = data.substring(index+40, index+42);
				String humidityyh  = data.substring(index+42, index+44);
				String humidityy  = humidityyh + humidityyl;
				float y = Integer.parseInt(humidityy, 16);
				int humidity =  (int) (- 4.0 + 0.0405 * y - 0.0000028 * y * y);
				
				weather.setTemperature(temperture);
				weather.setHumidity(humidity);
				
				//solarRation 单位：W/m2辐照度
				String readcl = data.substring(index+52, index+54);
				String readch = data.substring(index+54, index+56);
				String readc = readch + readcl;
				float reADC = Integer.parseInt(readc, 16);
				String solaradcl = data.substring(index+44, index+46);
				String solaradch = data.substring(index+46, index+48);
				String solaradc = solaradch + solaradcl;
				float solarADC = Integer.parseInt(solaradc, 16);
				float solarRadiation =  (float) (1225*solarADC/reADC/1.67);
				solarRadiation = (float)(Math.round(solarRadiation*100))/100;
				weather.setSolarRadiation(solarRadiation);
				/*System.out.println(windSpeed);
				System.out.println(windDir);
				System.out.println(temperture);
				System.out.println(humidity);
				System.out.println(solarRadiation);*/
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
		if(weather!=null){
			Timestamp date = new Timestamp(new Date().getTime());
			weather.setDate(date);
		}
		return weather;
	}







}