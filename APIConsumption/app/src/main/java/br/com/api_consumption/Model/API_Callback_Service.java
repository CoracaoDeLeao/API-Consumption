package br.com.api_consumption.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Interface responsável por definir as chamadas da API utilizando Retrofit.
public interface API_Callback_Service {
    // Define a requisição GET para obter dados com um formato JSON.
    @GET("dados?formato=json")
    Call<List<ApiResponse>> getDados( // Método que retorna os dados da API.
                                      @Query("dataInicial") String dataInicial, // Parâmetro para a data inicial.
                                      @Query("dataFinal") String dataFinal // Parâmetro para a data final.
    );
}
