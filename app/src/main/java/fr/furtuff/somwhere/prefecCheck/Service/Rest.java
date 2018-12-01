package fr.furtuff.somwhere.prefecCheck.Service;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Rest {
    private static final String baseUrl = "http://www.haute-garonne.gouv.fr/booking/create/7736/";
    private static Retrofit retrofit = null;
    private static Rest rest = null;
    private static Prefecture prefecture = null;

    private Rest() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        if (prefecture == null) {
            prefecture = retrofit.create(Prefecture.class);
        }
    }

    public static Rest getInstance() {
        if (rest == null) {
            rest = new Rest();
        }
        return rest;
    }

    public Prefecture getPrefecture() {
        return prefecture;
    }
}
