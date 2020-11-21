package com.example.listadetarefas;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TarefaDAO {

    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static String inserir(final Tarefa tarefa){
        Map<String, Object> tarefaMap = new HashMap<>();
        tarefaMap.put("tarefa", tarefa.getTarefa());
        tarefaMap.put("feita", tarefa.isFeita());

        db.collection("tarefas")
                    .add(tarefaMap)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(Tarefa.TAG, "Documento adicionado com o Id: "+documentReference.getId());
                            tarefa.setId(documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(Tarefa.TAG, "Erro ao adicionar documento.");
                        }
                    });
            return tarefa.getId();
    }

    public static ArrayList<Tarefa> recuperarTodas(final MainActivity activity){
        final ArrayList<Tarefa> tarefas = new ArrayList<>();
        db.collection("tarefas")
                .get()
                .addOnCompleteListener(activity, new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document: task.getResult()) {
                                Log.d(Tarefa.TAG, document.getId() + " => " + document.getData());
                                Tarefa tarefa = new Tarefa();
                                tarefa.setId(document.getId());
                                tarefa.setTarefa(document.get("tarefa").toString());
                                tarefa.setFeita(Boolean.parseBoolean(document.get("feita").toString()));
                                tarefas.add(tarefa);
                            }
                            activity.setAdapter();
                        } else {
                            Log.w(Tarefa.TAG, "Erro na recuperação dos documentos.", task.getException());
                        }
                    }
                });
        return tarefas;
    }

    public static void alterarTarefa(final String id, boolean status) {
        db.collection("tarefas").document(id).update("feita", !status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Tarefa.TAG, "Documento alterado: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tarefa.TAG, "Erro ao alterar tarefa.", e);
                    }
                });
    }

    public static void apagaTarefa(final String id){
        db.collection("tarefas").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(Tarefa.TAG, "Documento apagado: " + id);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tarefa.TAG, "Erro ao apagar documento.", e);
                    }
                });
    }

}
