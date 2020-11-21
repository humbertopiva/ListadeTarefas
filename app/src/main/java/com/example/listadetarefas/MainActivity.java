package com.example.listadetarefas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private EditText txtTarefa;
    private ArrayList<Tarefa> tarefas;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtTarefa = findViewById(R.id.tarefa);
        listView = findViewById(R.id.lista);
        tarefas = TarefaDAO.recuperarTodas(this);
        Log.d(Tarefa.TAG, "lista de tarefas" + tarefas);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TarefaDAO.alterarTarefa(tarefas.get(i).getId(), tarefas.get(i).isFeita());
                setAdapter();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long l) {
                TarefaDAO.apagaTarefa(tarefas.get(i).getId());
                tarefas.remove(i);
                setAdapter();
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setAdapter(){
        String status;
        ArrayList<String> descricao = new ArrayList<>();
        //ordena o arraylist de tarefas
        Collections.sort(tarefas);
        for(Tarefa tarefa: tarefas){
            if (tarefa.isFeita()) {
                status = "(Concluída)";
            } else {
                status = "(Pendente)";
            }
            descricao.add(tarefa.getTarefa() + " " + status);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, descricao);
        listView.setAdapter(adapter);
    }

    public void inserirTarefa(View view) {
        String descricao = txtTarefa.getText().toString();
        Tarefa tarefa = new Tarefa();
        tarefa.setTarefa(descricao);
        String id = TarefaDAO.inserir(tarefa);
        tarefa.setId(id);
        tarefas.add(tarefa);
        txtTarefa.setText("");
        setAdapter();
    }
}