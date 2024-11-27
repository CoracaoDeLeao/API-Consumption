package br.com.api_consumption;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import br.com.api_consumption.Model.API_Callback_Service;
import br.com.api_consumption.Model.ApiResponse;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Declaração de variáveis para componentes da interface.
    EditText datePickerInicial; // Campo de entrada para a data inicial.
    EditText datePickerFinal;   // Campo de entrada para a data final.
    EditText campoValor;        // Campo de entrada para o valor monetário.
    CardView cardView;          // Componente visual para exibir os resultados.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ativa o comportamento "Edge-to-Edge", que permite que a interface aproveite a tela inteira.
        EdgeToEdge.enable(this);

        // Define o layout da Activity.
        setContentView(R.layout.activity_main);

        // Configura o listener para ajustar o padding da interface com base nos insets do sistema (ex: barras de navegação e status).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Define o Locale global da aplicação para português do Brasil, garantindo formatação correta de números e datas.
        Locale.setDefault(new Locale("pt", "BR"));

        // Inicializa as referências dos componentes da interface com base no layout.
        campoValor = findViewById(R.id.campoValor);
        cardView = findViewById(R.id.cardView);
        cardView.setVisibility(View.GONE); // Oculta o CardView inicialmente.

        datePickerInicial = findViewById(R.id.datePickerInicial);
        datePickerFinal = findViewById(R.id.datePickerFinal);
    }

    // Método para realizar uma chamada à API de forma assíncrona e obter dados com base nas datas fornecidas.
    public CompletableFuture<List<Double>> getAPI(String dataInicial, String dataFinal) {
        // Instancia o serviço da API usando Retrofit.
        API_Callback_Service apiService = RetrofitClient.getClient().create(API_Callback_Service.class);

        // Retorna um CompletableFuture para realizar a operação em uma thread separada.
        return CompletableFuture.supplyAsync(() -> {
            Call<List<ApiResponse>> call = apiService.getDados(dataInicial, dataFinal);

            try {
                // Realiza a chamada síncrona à API.
                Response<List<ApiResponse>> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    List<Double> valores = new ArrayList<>();
                    for (ApiResponse item : response.body()) {
                        try {
                            // Converte o valor recebido da API para Double.
                            valores.add(Double.parseDouble(item.getValor()));
                        } catch (NumberFormatException e) {
                            // Ignora valores inválidos e registra um aviso no log.
                            Log.w("getAPI", "Valor inválido ignorado: " + item.getValor(), e);
                        }
                    }
                    return valores; // Retorna os valores válidos.
                } else {
                    // Lança uma exceção em caso de resposta com erro.
                    throw new RuntimeException("Erro na resposta: Código " + response.code());
                }
            } catch (IOException e) {
                // Lança uma exceção em caso de erro de comunicação com a API.
                throw new RuntimeException("Erro na comunicação com a API", e);
            }
        });
    }

    // Método chamado ao clicar no botão "Corrigir".
    public void corrigir(View view) {
        // Obtém os valores inseridos nos campos de data.
        String dataInicial = datePickerInicial.getText().toString();
        String dataFinal = datePickerFinal.getText().toString();

        // Verifica se os campos de data estão preenchidos.
        if (dataInicial.isEmpty() || dataFinal.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha as datas!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtém o valor monetário inserido e realiza a validação.
        String valorStr = campoValor.getText().toString();
        String valorLimpo = valorStr.replaceAll("[^\\d,]", "").replace(",", "."); // Remove caracteres inválidos e substitui vírgula por ponto.

        final double valor;

        try {
            // Converte o valor limpo para Double.
            valor = valorStr.isEmpty() ? 0 : Double.parseDouble(valorLimpo);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Valor inválido!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Realiza a chamada à API e processa o resultado.
        getAPI(dataInicial, dataFinal)
                .thenApply(lista -> calcularValorAjustado(valor, lista)) // Ajusta o valor inicial com base nas taxas recebidas.
                .thenAccept(valorFinal -> runOnUiThread(() -> { // Atualiza a UI na thread principal.
                    exibirResultados(valor, valorFinal);
                }))
                .exceptionally(throwable -> {
                    // Trata exceções e exibe uma mensagem de erro.
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Erro ao obter dados: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("corrigir", "Erro ao processar os dados da API", throwable);
                    });
                    return null;
                });
    }

    // Exibe os resultados processados no CardView.
    private void exibirResultados(double valorOriginal, double valorAjustado) {
        TextView valorPrincipal = findViewById(R.id.valor_principal);
        TextView valorSecundario = findViewById(R.id.valor_secundario);

        // Formata os valores como moeda usando o locale atual.
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        cardView.setVisibility(View.VISIBLE); // Torna o CardView visível.
        valorPrincipal.setText(currencyFormat.format(valorOriginal)); // Exibe o valor original.
        valorSecundario.setText(currencyFormat.format(valorAjustado)); // Exibe o valor ajustado.
    }

    // Converte uma taxa percentual para decimal.
    public static double converterParaDecimal(double porcentagem) {
        return porcentagem / 100.0;
    }

    // Calcula o valor ajustado aplicando as taxas de inflação recebidas.
    public static double calcularValorAjustado(double valorInicial, List<Double> taxas) {
        double valorAjustado = valorInicial;

        for (double taxa : taxas) {
            // Aplica cada taxa como um aumento percentual.
            valorAjustado *= (1 + converterParaDecimal(taxa));
        }

        return valorAjustado; // Retorna o valor ajustado final.
    }
}