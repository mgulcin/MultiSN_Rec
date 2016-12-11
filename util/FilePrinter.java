package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class FilePrinter {


	public void printString(String path, String string) throws FileNotFoundException
	{
		FileOutputStream fos;
		fos = new FileOutputStream(path,true);
		PrintStream ps = new PrintStream(fos);
		
		ps.println(string);
		
		ps.flush();
		ps.close();
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void convertToCsv(ResultSet rs, String path) throws SQLException, FileNotFoundException {
		PrintWriter csvWriter = new PrintWriter(new File(path)) ;
		ResultSetMetaData meta = rs.getMetaData() ; 
		int numberOfColumns = meta.getColumnCount() ; 
		String dataHeaders = meta.getColumnName(1);
		for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) { 
			dataHeaders += "," + meta.getColumnName(i) ;
		}
		csvWriter.println(dataHeaders) ;
		while (rs.next()) {
			String row =  rs.getString(1)  ; 
			for (int i = 2 ; i < numberOfColumns + 1 ; i ++ ) {
				row += "," + rs.getString(i) + "" ;
			}
			csvWriter.println(row) ;
		}
		csvWriter.close();
	}

}
