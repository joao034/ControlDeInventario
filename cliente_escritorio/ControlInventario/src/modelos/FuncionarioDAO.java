/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelos;

import comMdf.devazt.networking.HttpClient;
import comMdf.devazt.networking.OnHttpRequestComplete;
import comMdf.devazt.networking.Response;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Usuario
 */
public class FuncionarioDAO implements Crud{

    @Override
    public List<Funcionario> listarFuncionarios() {
      List<Funcionario> data=new ArrayList();
        HttpClient cliente =new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if(status.isSuccess()){
                    try{
                    System.out.println(status.getResult()); 
                    JSONObject estudiantesArray=new JSONObject(status.getResult());
                    for (int i = 0;; i++) {
                        Object[] obj=new Object[4];
                        obj[0]=estudiantesArray.getJSONObject(""+i+"").get("ID_FUN").toString();
                        obj[1]=estudiantesArray.getJSONObject(""+i+"").get("NOM_FUN").toString();
                        obj[2]=estudiantesArray.getJSONObject(""+i+"").get("APE_FUN").toString();
                        obj[3]=estudiantesArray.getJSONObject(""+i+"").get("NUM_ACT_FUN").toString();

                        data.add(new Funcionario(obj[0].toString(), obj[1].toString(), obj[2].toString(), Integer.valueOf(obj[3].toString())));
                    }
                    }catch(JSONException mes){
                        System.out.println(mes.getMessage());
                    }
                }
            }
        } );
        cliente.excecute("http://localhost/servicios/cargarFuncionarios.php");
        return data;
    }
}