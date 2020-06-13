package util;


import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;






public class CalculoIdade {
	
	 
	

	public static int idade(final LocalDate dataNascimento) {
		
		
			final LocalDate dataAtual = LocalDate.now();
			final Period periodo = Period.between(dataNascimento, dataAtual);
		    
		
		
		return periodo.getYears();
	    
	    
	}
	
	public static LocalDate format(String texto) {
		
		
        LocalDate localDate = null;
        DateTimeFormatter formatter = null;
		
        try {
        	
        
		formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		//texto= "2000/11/12";
        localDate = LocalDate.parse(texto, formatter);
        System.out.println("Input Date?= "+ texto);
        System.out.println("Converted Date?= " + localDate + "\n");
        }catch (Exception ex) { 
        	//JOptionPane.showMessageDialog(null, "Digite uma data Valida");
        	
        }
		
		return localDate;
	}

}
