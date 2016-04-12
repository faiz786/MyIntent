package pl.mareklangiewicz.myutils.myhttp

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object OpenWeatherMap {

    const val URL = "http://api.openweathermap.org"

    class Clouds {
        var all: Int = 0
    }

    class Coord {
        var lat: Float = 0f
        var lon: Float = 0f
    }

    class City {
        var id: Long = 0
        var name: String? = null
        var coord: Coord? = null
        var country: String? = null
        var population: Long = 0
    }

    class Wind {
        var speed: Float = 0f
        var deg: Float = 0f
    }

    class Sys {
        var message: Float = 0f
        var country: String? = null
        var sunrise: Long = 0
        var sunset: Long = 0
    }

    class Weather {
        var id: Int = 0
        var icon: String? = null
        var description: String? = null
        var main: String? = null
    }

    class Main {
        var humidity: Int = 0
        var pressure: Float = 0f
        var temp_max: Float = 0f
        var sea_level: Float = 0f
        var temp_min: Float = 0f
        var temp: Float = 0f
        var grnd_level: Float = 0f
    }

    class Temp {
        var min: Float = 0f
        var max: Float = 0f
        var day: Float = 0f
        var night: Float = 0f
        var eve: Float = 0f
        var morn: Float = 0f
    }

    class Forecast {
        var id: Long = 0
        var dt: Long = 0
        var clouds: Clouds? = null
        var coord: Coord? = null
        var wind: Wind? = null
        var cod: Int = 0
        var sys: Sys? = null
        var name: String? = null
        var base: String? = null
        var weather: Array<Weather>? = null
        var main: Main? = null
    }

    class DailyForecast {
        var dt: Long = 0
        var temp: Temp? = null
        var pressure: Float = 0f
        var humidity: Int = 0
        var weather: Array<Weather>? = null
        var speed: Float = 0f
        var deg: Float = 0f
        var clouds: Int = 0
        var rain: Float = 0f
        var snow: Float = 0f
    }

    class Forecasts {
        var city: City? = null
        var cod: String? = null
        var message: String? = null
        var cnt: Long = 0
        var list: Array<Forecast>? = null
    }

    class DailyForecasts {
        var city: City? = null
        var cod: String? = null
        var message: String? = null
        var cnt: Long = 0
        var list: Array<DailyForecast>? = null
    }

    /**
     * Id list for cities are here http://bulk.openweathermap.org/sample/
     * Default units are Kelvin, you can change it to "metric" (Celsius), or "imperial" (Fahrenheit)
     */
    interface Service {

        @GET("/data/2.5/weather") fun getWeatherByLocationCall(
                @Query("appid") appid: String,
                @Query("lat") lat: Float,
                @Query("lon") lon: Float,
                @Query("units") units: String?): Call<Forecast>

        @GET("/data/2.5/weather") fun getWeatherByCityCall(
                @Query("appid") appid: String,
                @Query("q") city: String,
                @Query("units") units: String?): Call<Forecast>

        @GET("/data/2.5/weather") fun getWeatherByIdCall(
                @Query("appid") appid: String,
                @Query("id") id: Long,
                @Query("units") units: String?): Call<Forecast>

        @GET("/data/2.5/forecast") fun getForecastByLocationCall(
                @Query("appid") appid: String,
                @Query("lat") lat: Float,
                @Query("lon") lon: Float,
                @Query("cnt") cnt: Long,
                @Query("units") units: String?): Call<Forecasts>

        @GET("/data/2.5/forecast") fun getForecastByCityCall(
                @Query("appid") appid: String,
                @Query("q") city: String,
                @Query("cnt") cnt: Long,
                @Query("units") units: String?): Call<Forecasts>

        @GET("/data/2.5/forecast") fun getForecastByIdCall(
                @Query("appid") appid: String,
                @Query("id") id: Long,
                @Query("cnt") cnt: Long,
                @Query("units") units: String?): Call<Forecasts>

        @GET("/data/2.5/forecast/daily") fun getDailyForecastByLocationCall(
                @Query("appid") appid: String,
                @Query("lat") lat: Float,
                @Query("lon") lon: Float,
                @Query("cnt") cnt: Long,
                @Query("units") units: String?): Call<DailyForecasts>

        @GET("/data/2.5/forecast/daily") fun getDailyForecastByCityCall(
                @Query("appid") appid: String,
                @Query("q") city: String,
                @Query("cnt") cnt: Long,
                @Query("units") units: String?): Call<DailyForecasts>

        @GET("/data/2.5/forecast/daily") fun getDailyForecastByIdCall(
                @Query("appid") appid: String,
                @Query("id") id: Long,
                @Query("cnt") cnt: Long,
                @Query("units") units: String?): Call<DailyForecasts>
    }

    private val retrofit = Retrofit.Builder().baseUrl(URL).addConverterFactory(MoshiConverterFactory.create()).build()

    val service = retrofit.create(Service::class.java)


        //            THIS IS OLD JAVA WAY TO INJECT SOME REQUEST/RESPONSE LOGGING
        //            if(VV) {
        //                OkHttpClient client = new OkHttpClient();
        //                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //                client.interceptors().add(interceptor);
        //
        //                Retrofit loggingretrofit = new Retrofit.Builder()
        //                        .baseUrl(URL)
        //                        .client(client)
        //                        .addConverterFactory(MoshiConverterFactory.create())
        //                        .build();
        //
        //                return loggingretrofit.create(Service.class);
        //            }



}