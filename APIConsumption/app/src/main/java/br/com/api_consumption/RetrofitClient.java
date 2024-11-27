package br.com.api_consumption;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Classe responsável por configurar e fornecer a instância do Retrofit.
public class RetrofitClient {

    // URL base da API que será utilizada para fazer as requisições.
    private static final String BASE_URL = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.433/";

    // Instância estática do Retrofit, que será usada para realizar as requisições.
    private static Retrofit retrofit;

    // Método público e estático para obter a instância do Retrofit.
    public static Retrofit getClient() {
        // Verifica se a instância do Retrofit já foi criada.
        if (retrofit == null) {
            // Caso a instância não exista, cria uma nova.
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Define a URL base para as requisições.
                    .addConverterFactory(GsonConverterFactory.create()) // Usa o Gson para conversão de JSON para objetos Java.
                    .build(); // Cria a instância do Retrofit.
        }
        // Retorna a instância do Retrofit.
        return retrofit;
    }
}
