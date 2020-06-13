package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * 
 * @author Tiago Batista
 *
 *	A classe buscaCEP tem a funcionalidade de buscar o endereço pelo CEP o que diminui a 
 * digitação e erros durante o preenchimento do endereço
 *
 */


public class BuscaCEP {
	
	String json;
	String logradouro;
	String bairro;
	String cidade;
	String uf;
	
	
	
	public BuscaCEP() {
		
	}
	
	
	public  String[] buscaCEP(String cep) {
		
		//novoEndereco.setCep("53270-501");
		String array[] = new String[30];
		System.out.println(cep.length());
		try {
			
			
            URL url = new URL("http://viacep.com.br/ws/"+ cep +"/json");
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            
            StringBuilder jsonSb = new StringBuilder();
            
            
            
            
            
            br.lines().forEach(l -> jsonSb.append(l.trim()));
            
            
            json = jsonSb.toString();
            
            
           
           
            System.out.println(json);
            json = json.replaceAll("[{},:]", "");
            
            json = json.replaceAll("\"", "\n");                       
           // String array[] = new String[30];
            array = json.split("\n");
            System.out.println(json);
            System.out.println(array[0]);
            
              //logradouro = array[7];            
            
            
		} catch (Exception e) {
			array[0]=("");
			array[1]=("erro");
			Logger.getLogger(BuscaCEP.class.getName()).log(Level.SEVERE, null, e);
        }
		
		return array;
		
	}
	
	
		

		
		
	



	
}
	
	
	

